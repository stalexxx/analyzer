package com.aostrovskiy

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import java.io.IOException
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.time.LocalDateTime

sealed class AnalyzeResult {
    class Ok(val txNumber: Long, val txAverage: Double) : AnalyzeResult()
    class CsvParseErr(val ex: Exception) : AnalyzeResult()
    class DateFormatErr(val date: String) : AnalyzeResult()
    class DoubleFormatErr(val double: String) : AnalyzeResult()
}

class Analyzer(
    private val cvsFile: Path,
    private val from: LocalDateTime,
    private val to: LocalDateTime,
    private val merchant: String
) {

    enum class Type {
        PAYMENT, REVERSAL
    }

    // Current csv library can't handle 2 symbol separator (have type of char), so
    // unfortunately trim and " Amount" used
    private inline val CsvRow.id
        get() = this.getField("ID").trim()

    private inline val CsvRow.dateTime: LocalDateTime?
        get() = dateTimeStr.toDate()

    private inline val CsvRow.dateTimeStr: String
        get() = this.getField(" Date").trim()

    private inline val CsvRow.amountStr
        get() = this.getField(" Amount").trim()
    
    private inline val CsvRow.amount: Double?
        get() = try {
            amountStr.toDouble()
        } catch (e: Exception) {
            null
        }

    private inline val CsvRow.merchant
        get() = this.getField(" Merchant").trim()

    private inline val CsvRow.type: Type
        get() {
            val type = this.getField(" Type").trim()
            return Type.valueOf(type)
        }

    private val CsvRow.related: String
        get() = this.getField(" Related Transaction").trim()

    private val txIds: HashSet<String> = hashSetOf()

    /**
     * This is function that making analysis. We suppose that file is not huge, and can easyly be read
     * into memory.
     * Otherwise we should have stream reader and read from bottom so that we could read reversals
     * first
     */
    fun analyze(): AnalyzeResult {
        return try {
            val file = cvsFile.toFile()
            val csvReader = CsvReader()
            csvReader.setContainsHeader(true)
            csvReader.setErrorOnDifferentFieldCount(true)

            val csv = csvReader.read(file, StandardCharsets.UTF_8) ?: return AnalyzeResult.Ok(0, 0.0)
            doAnalyze(csv.rows)
        } catch (ex: IOException) {
            AnalyzeResult.CsvParseErr(ex)
        }
    }

    private fun doAnalyze(rows: List<CsvRow>): AnalyzeResult {
        var count = 0L;
        var sum = 0.0;

        rows
            .filter { it.merchant == this.merchant }
            .forEach { row ->

                val dateTime = row.dateTime ?: return AnalyzeResult.DateFormatErr(row.dateTimeStr);
                val type = row.type
                val amount = row.amount ?: return AnalyzeResult.DoubleFormatErr(row.amountStr);
                val id = row.id

                if (type == Type.PAYMENT && dateTime >= from && dateTime <= to) {
                    count += 1;
                    sum += amount
                    txIds += id
                } else if (type == Type.REVERSAL) {
                    if (row.related in txIds) {
                        count -= 1;
                        sum -= amount
                    }
                }
            }

        return AnalyzeResult.Ok(count, sum / count)
    }
}

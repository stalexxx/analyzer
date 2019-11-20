package com.aostrovskiy

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import java.io.File
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AnalyzeResult(val txNumber: Long, val txAverage: Double)

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
fun String.toDate(): LocalDateTime? = LocalDateTime.parse(this, dateTimeFormatter)!!

class Analyzer(private val config: Config) {

    val CsvRow.id
        get() = this.getField("ID").trim()

    val CsvRow.dateTime: LocalDateTime
        get() {
            //20/08/2018 12:50:02
            val formatter = dateTimeFormatter

            val dateStr = this.getField(" Date").trim()

            return LocalDateTime.parse(dateStr, formatter)
        }
    val CsvRow.amount
        get() = this.getField(" Amount").trim().toDouble()

    val CsvRow.merchant
        get() = this.getField(" Merchant").trim()

    val CsvRow.type: Type
        get() {
            val type = this.getField(" Type").trim()
            return Type.valueOf(type)
        }

    val CsvRow.related: String
        get() {
            return this.getField(" Related Transaction").trim()
        }

    val CsvRow.toTx
        get() = Tx(dateTime, amount, type)

    enum class Type {
        PAYMENT, REVERSAL
    }

    data class Tx(val dateTime: LocalDateTime, val amount: Double, val type: Type)

    private val txIds: HashSet<String> = hashSetOf()

    //we suppose file is small
    fun analyze(): AnalyzeResult {

        val file = config.cvsFile.toFile()
        val csvReader = CsvReader()
        csvReader.setContainsHeader(true)

        val csv = csvReader.read(file, StandardCharsets.UTF_8) ?: return AnalyzeResult(0, 0.0)

        var count = 0L;
        var sum = 0.0;

        csv.rows
            .filter { it.merchant == config.merchant }
            .forEach { row ->

                val dateTime = row.dateTime;
                val type = row.type
                val amount = row.amount
                val id = row.id

                if (type == Type.PAYMENT && dateTime.isAfter(config.from) && dateTime.isBefore(config.to)) {
                    count += 1;
                    sum += amount;
                    txIds += id
                } else if (type == Type.REVERSAL) {
                    if (txIds.contains(row.related)) {
                        count -= 1;
                        sum -= row.amount
                    }
                }
            }

        return AnalyzeResult(count, sum / count)
    }
}

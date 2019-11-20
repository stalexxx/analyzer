package com.aostrovskiy

import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.file.Paths
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AnalyzerTest {
    @Test
    fun `test empty`() {
        val analyzer = Analyzer("test_empty.csv".toPathFromResource(), LocalDateTime.MIN, LocalDateTime.MAX, "")
        val result = analyzer.analyze()

        assert(result is AnalyzeResult.Ok)
        result as AnalyzeResult.Ok
        assertEquals(result.txNumber, 0)
        assertEquals(result.txAverage, BigDecimal.ZERO)
    }

    @Test
    fun `test simple`() {
        val analyzer = Analyzer("test0.csv".toPathFromResource(), LocalDateTime.MIN, LocalDateTime.MAX, "")
        val result = analyzer.analyze()

        assert(result is AnalyzeResult.Ok)
        result as AnalyzeResult.Ok
        assertEquals(result.txNumber, 0)
        assertEquals(result.txAverage, BigDecimal.ZERO)
    }

    @Test
    fun `test wrong csv`() {
        val analyzer = Analyzer("test_wrong_csv.csv".toPathFromResource(), LocalDateTime.MIN, LocalDateTime.MAX, "")
        val result = analyzer.analyze()

        assert(result is AnalyzeResult.CsvParseErr)
    }

    @Test
    fun `test Jason's test case`() {
        val analyzer = Analyzer(
            "test1.csv".toPathFromResource(), "20/08/2018 12:00:00".toDateOrNull()!!,
            "20/08/2018 13:00:00".toDateOrNull()!!, "Kwik-E-Mart"
        )

        val result = analyzer.analyze()

        assert(result is AnalyzeResult.Ok)
        result as AnalyzeResult.Ok
        assertEquals(1, result.txNumber)
        assertEquals(result.txAverage, BigDecimal(BigInteger.valueOf(5999), 2))
    }

    private fun String.toPathFromResource() =
        Paths.get(this@AnalyzerTest.javaClass.classLoader.getResource(this)!!.toURI())
}
package com.aostrovskiy

import org.junit.Test
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.test.assertEquals

class AnalyzerTest {
    @Test
    fun `test empty`() {
        val cvsFile = this.javaClass.classLoader.getResource("test_empty.csv")!!.toPath()
        val analyzer = Analyzer(Config(cvsFile, LocalDateTime.MIN, LocalDateTime.MAX, ""))
        val (txNumber, txAverage) = analyzer.analyze()

        assertEquals(txNumber, 0)
        assertEquals(txAverage, 0.0)
    }

    @Test
    fun `test simple`() {
        val cvsFile = this.javaClass.classLoader.getResource("test0.csv")!!.toPath()
        val analyzer = Analyzer(Config(cvsFile, LocalDateTime.MIN, LocalDateTime.MAX, ""))
        val (txNumber, txAverage) = analyzer.analyze()

        assertEquals(txNumber, 0)
        assertEquals(txAverage, 0.0)
    }

    @Test
    fun `test Jason's test case`() {
        val cvsFile = this.javaClass.classLoader.getResource("test1.csv")!!.toPath()
        val analyzer = Analyzer(Config(cvsFile, "20/08/2018 12:00:00".toDate()!!,
        "20/08/2018 13:00:00".toDate()!!, "Kwik-E-Mart"))
        val (txNumber, txAverage) = analyzer.analyze()

        assertEquals( 1, txNumber)
        assert( txAverage `~==` 59.99);
    }
}

fun URL.toPath(): Path = Paths.get(this.toURI())

// Suppose that 0.000001 on any fiat currency is nothing
infix fun Double.`~==`(another: Double): Boolean {
    return abs(this - another) < 0.000001
}

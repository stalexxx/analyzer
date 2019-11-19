package com.aostrovskiy

import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals

class AnalyzerTest {
    @Test
    fun `test simple`() {
        val analyzer = Analyzer(Config("test0.csv", Instant.MIN, Instant.MAX))
        val (txNumber, txAverage) = analyzer.analyze()

        assertEquals(txNumber, 0)
        assertEquals(txAverage, 0.0)
    }
}

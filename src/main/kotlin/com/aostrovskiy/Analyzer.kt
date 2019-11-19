package com.aostrovskiy

data class AnalyzeResult(val txNumber: Long, val txAverage: Double)

class Analyzer(val config: Config) {
    fun analyze(): AnalyzeResult{

        return AnalyzeResult(0,0.0)
    }

}
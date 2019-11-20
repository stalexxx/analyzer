package com.aostrovskiy

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    AnalyzerTool().main(args)
}

class AnalyzerTool: CliktCommand() {
    private val csv: Path? by option(help = "CSV location").path()
    private val from: String? by option(help = "From date")
    private val to: String? by option(help = "To date")
    private val merchant: String? by option(help = "Merchant")

    override fun run() {
        val path  = csv?.toAbsolutePath() ?: exit("File not set or incorrect")
        val from = from?.toDate() ?: exit("From date not set or incorrect")
        val to = to?.toDate() ?: exit("To date not set or incorrect")
        val merchant = merchant ?: exit("Merchant not set or incorrect")

        val analyzer = Analyzer(path, from, to, merchant)
        when (val result = analyzer.analyze()) {
            is AnalyzeResult.Ok -> {
                println("Number of transactions = ${result.txNumber}")
                println("Average Transaction Value = ${result.txAverage.withPrecision(2)}")
            }
            is AnalyzeResult.CsvParseErr -> println("Incorrect CSV file: ${result.ex.message}")
            is AnalyzeResult.DateFormatErr -> println("Date format err: ${result.date}")
            is AnalyzeResult.DoubleFormatErr -> println("Double format err: ${result.double}")
        }
    }

    private fun Double.withPrecision(decimals: Int = 2) = "%.${decimals}f".format(this)

    private fun exit(msg: String): Nothing {
        println(msg)
        exitProcess(-1)
    }
}
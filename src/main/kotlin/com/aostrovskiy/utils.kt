package com.aostrovskiy

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val DATE_TIME_DEFAULT_PATTERN = "dd/MM/yyyy HH:mm:ss"

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_DEFAULT_PATTERN)

fun String.toDateOrNull(): LocalDateTime? = try {
    LocalDateTime.parse(this, dateTimeFormatter)
} catch (e: Exception) {
    null
}
fun main() {
    val map = hashMapOf<Int, Long>()
    map[1] = 5
    val l = map[1]
    val x: Int? = l?.toInt()
    println(l)
    println(x)

}
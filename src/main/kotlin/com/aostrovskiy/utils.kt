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

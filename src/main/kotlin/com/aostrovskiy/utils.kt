package com.aostrovskiy

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

fun String.toDate(): LocalDateTime? = try {
    LocalDateTime.parse(this, dateTimeFormatter)
} catch (e: Exception) {
    null
}

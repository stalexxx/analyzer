package com.aostrovskiy

import java.time.Instant

data class Config(val cvsFile: String, val from: Instant, val to: Instant)

fun parseConfig(args: Array<String>): Config {
    return Config("", Instant.MAX, Instant.MAX)
}
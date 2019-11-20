package com.aostrovskiy

import java.nio.file.Path
import java.time.LocalDateTime

data class Config(val cvsFile: Path, val from: LocalDateTime, val to: LocalDateTime, val merchant: String)
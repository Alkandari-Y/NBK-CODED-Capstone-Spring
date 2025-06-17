package com.project.banking.utils

import java.util.UUID
import kotlin.random.Random

fun generateAccountNumber(length: Int = 12): String {
    require(length >= 12) { "Length should be at least 12 to ensure randomness + uniqueness" }

    val prefixLength = length - 6
    val prefix = (1..prefixLength)
        .map { Random.nextInt(0, 10) }
        .joinToString("")

    val uuidSuffix = UUID.randomUUID().hashCode().toUInt().toString().take(6).padStart(6, '0')

    return prefix + uuidSuffix
}
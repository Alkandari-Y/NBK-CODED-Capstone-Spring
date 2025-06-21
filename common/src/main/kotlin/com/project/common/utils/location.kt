package com.project.common.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun convertDMStoDecimal(degrees: Int, minutes: Int, seconds: Int, direction: Char): Double {
    var decimal = degrees + (minutes.toDouble() / 60) + (seconds.toDouble() / 3600)
    if (direction == 'S' || direction == 'W') {
        decimal = -decimal
    }
    return decimal
}
fun parseCoordinate(value: Any): BigDecimal {
    return when (value) {
        is String -> {
            val cleaned = value.trim()
            if (cleaned.contains('°')) {
                DMSCoordinate.parse(cleaned).toDecimal()
            } else {
                try {
                    BigDecimal(cleaned)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Invalid decimal coordinate string: '$value'")
                }
            }
        }
        is Number -> BigDecimal.valueOf(value.toDouble())
        else -> throw IllegalArgumentException("Unsupported coordinate type: $value")
    }
}

data class DMSCoordinate(
    val degrees: Int,
    val minutes: Int,
    val seconds: Int,
    val direction: Char
) {
    companion object {
        private val dmsPattern = """(\d+)°(\d+)'(\d+)"([NSEW])""".toRegex()

        fun parse(dmsString: String): DMSCoordinate {
            val matchResult = dmsPattern.find(dmsString.trim())
                ?: throw IllegalArgumentException("Invalid DMS format. Expected format: 29°18'10\"N")

            return DMSCoordinate(
                degrees = matchResult.groupValues[1].toInt(),
                minutes = matchResult.groupValues[2].toInt(),
                seconds = matchResult.groupValues[3].toInt(),
                direction = matchResult.groupValues[4][0]
            )
        }
    }

    fun toDecimal(): BigDecimal {
        val decimal = BigDecimal(degrees) +
                BigDecimal(minutes).divide(BigDecimal(60), 8, RoundingMode.HALF_UP) +
                BigDecimal(seconds).divide(BigDecimal(3600), 8, RoundingMode.HALF_UP)

        return if (direction == 'S' || direction == 'W') {
            decimal.negate()
        } else {
            decimal
        }
    }
}

package day2

import java.lang.IllegalArgumentException
import java.util.*

enum class Direction {
    UP, DOWN, FORWARD;

    fun parse(stringValue: String): Direction {
        val potentialMatch = values().find { enumValue -> stringValue.uppercase().equals(enumValue.name) }
        return Optional.ofNullable(potentialMatch).orElseThrow { IllegalArgumentException("No enum matching $stringValue") }
    }


}

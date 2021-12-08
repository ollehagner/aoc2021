package day8

import readInput

fun main() {

    fun part1(input: List<String>) {
        val outputValues = input.flatMap { it.substringAfter("|").split(" ") }.filter { it.isNotEmpty() }
        val wantedDigitLengths = listOf(2,3,4,7)
        val total = outputValues.count { it.length in wantedDigitLengths }
        println("Day 8 part 1. Num of digits: $total")
    }

    fun mapInputToDigits(input: List<String>): List<Digit> {
        val segmentMapping = HashMap<String, String>()
        val digits = HashMap<Int, String>()
        digits[1] = input.first { it.length == 2 }
        digits[7] = input.first { it.length == 3 }
        digits[4] = input.first { it.length == 4 }
        digits[8] = input.first { it.length == 7 }

        segmentMapping["a"] = digits[7]!! - digits[1]!!

        digits[3] = input.filter { it.length == 5 }.first { (it - digits[7]!!).length == 2 }

        digits[9] = input.filter { it.length == 6 }.first { (it - digits[3]!!).length == 1 }

        segmentMapping["e"] = digits[8]!! - digits[9]!!

        segmentMapping["g"] = digits[9]!! - (digits[4]!! union digits[7]!!)
        segmentMapping["d"] = (digits[3]!! - digits[1]!!) - segmentMapping["a"]!! - segmentMapping["g"]!!

        digits[0] = digits[8]!! - segmentMapping["d"]!!

        digits[6] = input.filter { it.length == 6 }.filterNot { it.containsSameChars(digits[0]!!) || it.containsSameChars(digits[9]!!) }.first()

        digits[5] = digits[6]!! - segmentMapping["e"]!!

        segmentMapping["c"] = digits[8]!! - digits[6]!!
        segmentMapping["f"] = digits[1]!! - segmentMapping["c"]!!

        digits[2] = digits[3]!! - segmentMapping["f"]!! + segmentMapping["e"]
        return digits.entries
            .map { entry -> Digit(entry.value, entry.key) }
    }

    fun part2(input: List<String>) {
        val sum = input
            .map { inputRow ->
                val digits = mapInputToDigits(inputRow.substringBefore("|").split(" ").filter { it.isNotEmpty() })
                inputRow.substringAfter("|").split(" ").filter { it.isNotEmpty() }
                    .map { outputValue -> digits.first { it.segments.containsSameChars(outputValue) }.intValue }
                    .fold("") { acc, value -> acc + value }
            }
            .sumOf { it.toInt() }
        println("Day 8 part 2. Sum of outputs is $sum")
    }

    val smallTestInput = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"
    val testInput = readInput("day8/day08_test")
    val input = readInput("day8/day08")
    part1(input)
    part2(input)

}
fun String.toList(): List<String> {
    return this.split("").filter { it.isNotEmpty() }.toList()
}

infix fun String.union(other :String): String {
    return (this.toList() + other.toList()).toSet().reduce(String::plus)
}

operator fun String.minus(other: String): String {
    return this.toList().filterNot { it in other }.reduce(String::plus)
}

fun String.containsSameChars(other: String): Boolean {
    return this.length == other.length && this.toList().containsAll(other.toList())
}

private data class Digit(val segments: String, val intValue: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Digit) return false
        return other.segments.length == segments.length && other.segments.toList().containsAll(segments.toList())
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

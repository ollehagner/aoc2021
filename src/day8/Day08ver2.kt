package day8

import readInput

fun main() {

    data class Digit(val segments: Set<Char>, val value: Int) {

    }

    fun part1(input: List<String>) {
        val outputValues = input.flatMap { it.substringAfter('|').split(' ') }.filter { it.isNotEmpty() }
        val wantedDigitLengths = listOf(2,3,4,7)
        val total = outputValues.count { it.length in wantedDigitLengths }
        println("Day 8 part 1. Num of digits: $total")
    }

    fun mapInputToDigits(input: List<Set<Char>>): List<Digit> {
        val segmentMapping = HashMap<Char, Char>()
        val digits = Array<Set<Char>>(10) { emptySet() }

        digits[1] = input.first { it.size == 2 }
        digits[7] = input.first { it.size == 3 }
        digits[4] = input.first { it.size == 4 }
        digits[8] = input.first { it.size == 7 }

        segmentMapping['a'] = (digits[7] - digits[1]).first()

        digits[3] = input.filter { it.size == 5 }.first { (it - digits[7]).size == 2 }

        digits[9] = input.filter { it.size == 6 }.first { (it - digits[3]).size == 1 }

        segmentMapping['e'] = (digits[8] - digits[9]).first()
        segmentMapping['g'] = (digits[9] - (digits[4] + digits[7])).first()
        segmentMapping['d'] = ((digits[3] - digits[1]) - setOf(segmentMapping['a'], segmentMapping['g'])).first()!!

        digits[0] = digits[8] - setOf(segmentMapping['d']!!)

        digits[6] = input.filter { it.size == 6 }.filterNot { it == digits[0] || it == digits[9] }.first()

        digits[5] = digits[6] - segmentMapping['e']!!

        segmentMapping['c'] = (digits[8] - digits[6]).first()
        segmentMapping['f'] = (digits[1] - segmentMapping['c']!!).first()

        digits[2] = digits[3] - segmentMapping['f']!! + segmentMapping['e']!!
        return digits
            .mapIndexed { value, segments -> Digit(segments, value) }
    }

    fun part2(input: List<String>) {
        val sum = input
            .map { inputRow ->
                val digits = mapInputToDigits(inputRow.substringBefore("|").split(" ").map { it.toSet() })
                inputRow.substringAfter("|")
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.toSet() }
                    .map { outputValue -> digits.first { it.segments == outputValue }.value }
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




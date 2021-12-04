package day3

import readInput

fun main() {

    fun part1(input: List<IntArray>) {
        val gamma = input
            .reduce { acc, values ->
                values.forEachIndexed { index, value ->
                    acc[index] += value
                }
                acc
            }
            .map { if(it > (input.size - it)) 1 else 0 }
            .fold("") { acc, value -> acc + value }
            .toInt(2)

        val epsilon = gamma.toString(2)
            .map { it.digitToInt() xor 1 }
            .fold("") { acc, value -> acc + value }
            .toInt(2)
        println("Day 3 part 1. Gamma=$gamma, epsilon=$epsilon. Power consumption = ${gamma * epsilon}")

    }


    fun bitCriteria(values: List<IntArray>, indexToLookAt: Int, numberToKeepFunction: (List<Int>) -> Int): Int {
        val numberToKeep = numberToKeepFunction(values.map{ it[indexToLookAt]})

        val filteredValues = values.filter { it[indexToLookAt] == numberToKeep }
        if(filteredValues.size == 1) {
            return filteredValues.first().fold("") { acc, value -> acc + value }.toInt(2)
        } else {
            return bitCriteria(filteredValues, indexToLookAt + 1, numberToKeepFunction)
        }
    }

    fun part2(input: List<IntArray>) {
        val oxygenGeneratorFunction = { numbers: List<Int> -> if (numbers.sum() >= (numbers.size - numbers.sum())) 1 else 0 }
        val co2ScrubberFunction = { numbers: List<Int> -> if (numbers.sum() < (numbers.size - numbers.sum())) 1 else 0 }

        val oxygenGeneratorRating = bitCriteria(input, 0, oxygenGeneratorFunction)
        val co2ScrubberRating = bitCriteria(input, 0, co2ScrubberFunction)
        println("Day 3 part 2. Oxygen generator rating: $oxygenGeneratorRating, CO2 scrubber rating: $co2ScrubberRating. Life support rating: ${oxygenGeneratorRating * co2ScrubberRating}")
    }


    fun toIntArray(input: List<String>): List<IntArray> {
        return input.map { it.split("").filter { it.isNotEmpty() } }
            .map { it.map { stringValue -> stringValue.toInt()}}
            .map { it.toIntArray() }
    }

    val testInput = toIntArray(readInput("day3/day03_test"))
    val input = toIntArray(readInput("day3/day03"))
    part1(input)
    part2(input)


}

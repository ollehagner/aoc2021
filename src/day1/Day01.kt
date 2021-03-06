package day1

import readInput

fun main() {
    fun part1(input: List<String>) {

        val count = input.windowed(2)
            .map { values -> Pair(values.first().toInt(), values.last().toInt()) }
            .count { pair -> pair.first < pair.second }
        println("Day 1 part 1. Got $count increases")

    }

    fun part2(input: List<String>) {
        val count = input
            .map(String::toInt)
            .windowed(3)
            .map { values -> values.sum() }
            .windowed(2)
            .map { values -> Pair(values.first(), values.last()) }
            .count { pair -> pair.first < pair.second }
        println("Day 1 part 2. Got $count increases")

    }

    val testInput = readInput("day1/Day01_test")
    val input = readInput("day1/Day01")

    part1(input)
    part2(input)
}

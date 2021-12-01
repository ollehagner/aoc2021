package day1

import readInput

fun main() {
    fun part1(input: List<String>) {

        val count = input.windowed(2)
            .map { values -> Pair(values.first().toInt(), values.last().toInt()) }
            .filter { pair -> pair.first < pair.second }
            .count();
        println("Got $count increases")

    }

    fun part2(input: List<String>) {
        val count = input
            .map(String::toInt)
            .windowed(3)
            .map { values -> values.sum() }
                .windowed(2)
                .map { values -> Pair(values.first(), values.last()) }
                .filter { pair -> pair.first < pair.second }
            .count()
        println("Got $count increases")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day1/Day01_test")
    val input = readInput("day1/Day01")

//    part1(input)
    part2(input)
}

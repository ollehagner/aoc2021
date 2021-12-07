package day6

import readInput

fun main() {

    fun iterate(fishes: List<Int>): List<Int> {
        return fishes
            .map { fish ->
                if(fish == 0) {
                    listOf(6, 8)
                } else {
                    listOf(fish - 1)
                }
            }
            .flatten()
    }

    fun part1(input: List<Int>) {
        val result = IntRange(1, 80)
            .fold(input) { acc, _ -> iterate(acc) }
        println("Day 6 part 1. Num of fishes ${result.size}")
    }

    fun part2(input: Map<Int, Long>) {
        val result = IntRange(1, 256)
            .fold(input) { acc, _ ->
                val newMap = HashMap<Int, Long>()
                acc
                    .entries
                    .map { entry ->
                        if(entry.key == 0) {
                            newMap[8] = entry.value
                            newMap.merge(6, entry.value) { existingValue, newValue -> existingValue + newValue }
                        } else {
                            newMap.merge(entry.key - 1, entry.value) { existingValue, newValue -> existingValue + newValue }
                        }
                    }
                newMap
            }
            .values
            .sum()
        println("Day 6 part 2. Num of fishes ${result}")

    }

    val testInput = listOf(3,4,3,1,2)
    val input = readInput("day6/day06").first().split(",").map { it.toInt() }
    part1(input)
    part2(input.groupingBy { it }.eachCount().mapValues { it.value.toLong() })


}

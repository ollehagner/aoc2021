package day14

import readInput


fun main() {

    fun polymerSequence(start: String, rules: Map<String, String>): Sequence<String> = sequence {
        var current = start

        while (true) {
            val transformed = current.toCharArray()
                .toList()
                .windowed(2)
                .map { it.joinToString("") }
                .map {
                    when (rules.containsKey(it)) {
                        true -> it.first() + rules[it]!! + it.last()
                        false -> it
                    }
                }
                .toList()
            current = transformed
                .dropLast(1)
                .map { it.substring(0, 2) }
                .joinToString("") + transformed.last()
            yield(current)
        }

    }

    fun parseRules(input: List<String>): Map<String, String> {
        return input
            .takeLastWhile { it.isNotEmpty() }
            .associate { it.substringBefore("->").trim() to it.substringAfter("->").trim() }
    }

    fun part1(input: List<String>) {
        val elementCount = polymerSequence(input.first(), parseRules(input))
            .take(10)
            .last()
            .toCharArray()
            .map { it.toString() }
            .groupingBy { it }
            .eachCount()
        val mostCommon = elementCount.maxOf { it.value }
        val leastCommon = elementCount.minOf { it.value }
        println("Day 14 part 1. Most common subtracted by least common = ${mostCommon - leastCommon}")


    }

    fun part2(input: List<String>) {
        val elementCount = polymerSequence(input.first(), parseRules(input))
            .take(40)
            .last()
            .toCharArray()
            .map { it.toString() }
            .groupingBy { it }
            .eachCount()
        val mostCommon = elementCount.maxOf { it.value }
        val leastCommon = elementCount.minOf { it.value }
        println("Day 14 part 2. Most common subtracted by least common = ${mostCommon - leastCommon}")
    }

    val testinput = readInput("day14/day14_test")
    val input = readInput("day14/day14")

    part1(input)
    part2(testinput)





}

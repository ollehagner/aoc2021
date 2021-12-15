package day14

import readInput


fun main() {

    fun polymerSequence(start: String, rules: Map<String, String>): Sequence<Map<String, Long>> = sequence {
        var pairCount = start.toCharArray()
            .toList()
            .windowed(2)
            .map { it.joinToString("") }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMap()

        var elementCount = start.toCharArray()
            .toList()
            .map { it.toString() }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }
            .toMutableMap()

        while (true) {
            pairCount = pairCount.entries
                .flatMap { entry ->
                    val createdElement = rules[entry.key]!!
                    val numOfCreatedElements = entry.value
                    elementCount.merge(createdElement, numOfCreatedElements) { existingNumOfElements, created -> existingNumOfElements + created }

                    val newElementPairs = listOf("${entry.key.first()}${createdElement}", "$createdElement${entry.key.last()}")
                    newElementPairs
                        .map { elementPair: String -> elementPair to numOfCreatedElements }
                }
                .fold(HashMap()) { acc, entry ->
                    acc.merge(entry.first, entry.second){ existingValue, newValue -> existingValue + newValue}
                    acc
                }
            yield(elementCount)
        }

    }

    fun parseRules(input: List<String>): Map<String, String> {
        return input
            .takeLastWhile { it.isNotEmpty() }
            .associate { it.substringBefore("->").trim() to it.substringAfter("->").trim() }
    }

    fun calculateAnswer(elementCount: Map<String, Long>): Long {
        val mostCommon = elementCount.maxOf { it.value }
        val leastCommon = elementCount.minOf { it.value }
        return mostCommon - leastCommon
    }

    fun part1(input: List<String>) {
        val elementCount = polymerSequence(input.first(), parseRules(input)).take(10).last()
        println("Day 14 part 1. Most common subtracted by least common = ${calculateAnswer(elementCount)}")
    }

    fun part2(input: List<String>) {
        val elementCount = polymerSequence(input.first(), parseRules(input)).take(40).last()
        println("Day 14 part 2. Most common subtracted by least common = ${calculateAnswer(elementCount)}")
    }

    val testinput = readInput("day14/day14_test")
    val input = readInput("day14/day14")

    part1(input)
    part2(input)





}

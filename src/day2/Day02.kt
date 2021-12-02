package day2

import readInput;

fun main() {

    fun part1(input: List<String>) {
        val finalPosition = input.map { value -> value.split(" ") }
            .map { splitted ->
                val direction = Direction.valueOf(splitted.first().uppercase())
                val magnitude = splitted.last().toInt()
                Vector(direction, magnitude)
            }
            .fold(Position(0, 0)) { position, vector -> position.move(vector) }
        println("Day 2 part 1. Final result : ${finalPosition.x * finalPosition.depth}")
    }

    val testInput = readInput("day2/day02_test")
    val input = readInput("day2/day02")
    part1(input)
}

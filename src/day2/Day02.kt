package day2

import readInput;

fun main() {

    fun part1(input: List<Command>) {
        val finalPosition = input
            .fold(Position(0, 0)) { position, vector -> position.execute(vector) }
        println("Day 2 part 1. Final result : ${finalPosition.x * finalPosition.depth}")
    }

    val testInput = readInput("day2/day02_test")
        .map { value -> Command(value) }
    val input = readInput("day2/day02")
        .map { value -> Command(value) }
    part1(input)
}

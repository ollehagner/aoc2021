package day2

import readInput;

fun main() {

    fun part1(input: List<Command>) {
        val finalPosition = input
            .fold(Position(0, 0)) { position, command -> position.move(command.direction, command.magnitude) }
        println("Day 2 part 1. Final result : ${finalPosition.x * finalPosition.depth}")
    }

    fun part2(input: List<Command>) {
        val finalSubmarineStatus = input
            .fold(SubmarineStatus(Position(0, 0), 0)) { status, command ->
                status.executeCommand(command)
            }
        println("Day 2 part 2. Final result: ${finalSubmarineStatus.position.x * finalSubmarineStatus.position.depth}")
    }

    val testInput = readInput("day2/day02_test")
        .map { value -> Command(value) }
    val input = readInput("day2/day02")
        .map { value -> Command(value) }

    part1(input)
    part2(input)

}

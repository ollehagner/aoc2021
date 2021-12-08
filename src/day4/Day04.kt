package day4

import common.Grid
import common.Point

import readInput


fun main() {

    fun part1(boards: List<BingoBoard>, numbers: List<Int>) {
        for(number in numbers) {
            boards.forEach { it.mark(number) }
            val potentialBingoBoard = boards.find { it.hasBingo()}
            if(potentialBingoBoard != null) {
                println("Day 4 part 1. Score is ${potentialBingoBoard.score() * number}")
                break
            }
        }
    }

    fun recursiveRound(boards: List<BingoBoard>, numbers: List<Int>): Pair<BingoBoard, Int> {
        boards.forEach { it.mark(numbers.first())}
        val nonBingoBoards = boards.filter { !it.hasBingo()}
        if(nonBingoBoards.isEmpty()) {
            return Pair(boards.first(), numbers.first())
        } else {
            return recursiveRound(nonBingoBoards, numbers.drop(1))
        }
    }

    fun part2(boards: List<BingoBoard>, numbers: List<Int>) {
        val lastToWin = recursiveRound(boards, numbers)
        println("Day 4 part 2. Score is ${lastToWin.first.score() * lastToWin.second}")
    }

    val testInput = readInput("day4/day04_test")
    val input = readInput("day4/day04")
    val numbers = input.first().split(",").map(String::toInt)

    val boards = input
            .drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { BingoBoard(it) }
    part1(boards, numbers)
    part2(boards, numbers)

}


private class BingoBoard {

    constructor(values: List<String>) {
        values
                .map { it.trim().split("\\s+".toPattern()) }
                .forEachIndexed { y, rowValues ->
                    rowValues.forEachIndexed { x, value -> grid.set(Point(x, y), BingoField(value.toInt(), false)) }
                }
    }

    private val grid = Grid<BingoField>()

    fun mark(value: Int) {
        grid.values()
                .filter { it.value == value }
                .forEach { it.marked = true }
    }

    fun hasBingo(): Boolean {
        return listOf(grid.rows(), grid.columns()).flatten()
                .map { rowOrColumn -> rowOrColumn.stream().allMatch { it.marked }}
                .any { it }
    }

    fun score(): Int {
        return grid.values()
                .filter { !it.marked }
                .sumOf { it.value }
    }

    fun print() {
        println(grid)
    }

}

private data class BingoField(val value: Int, var marked: Boolean) {
    override fun toString(): String {
        return "$value".padEnd(3, ' ')
     }
}

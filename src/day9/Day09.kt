package day9

import common.Direction.*
import common.Grid
import common.Point
import readInput


fun main() {

    fun adjacentPoints(point: Point): List<Point> {
        return listOf(UP, DOWN, LEFT, RIGHT )
            .map { point.move(it, 1) }
    }

    fun getLowPoints(grid: Grid<Int>): Set<Point> {
        return grid.allPoints()
            .map { Pair(it, adjacentPoints(it)) }
            .map { (currentPoint, adjacentPoints) -> Pair(currentPoint, grid.valueOfExistingPoints(adjacentPoints)) }
            .filter { (current, adjacentPointValues) -> adjacentPointValues.all { adjacentPointValue -> adjacentPointValue > grid.valueOf(current) } }
            .map { it.first }
            .toSet()
    }

    fun part1(input: List<List<Int>>) {
        val grid = Grid(input)
        val sumOfLowPoints = getLowPoints(grid)
            .map { grid.valueOf(it) + 1 }
            .sum()
        println("Day 9 part 1. Sum of low points: $sumOfLowPoints")
    }

    /*
        0,0 ->
     */

    fun getBasinPoints(grid: Grid<Int>, pointsToCheck: Set<Point>) : Set<Point> {
        val adjacentBasinPoints = pointsToCheck
            .flatMap { adjacentPoints(it) }
            .filter { grid.hasValue(it) }
            .filter { grid.valueOf(it) < 9 }
            .filter { it !in pointsToCheck }
            .toSet()
        if(adjacentBasinPoints.isEmpty()) {
            return pointsToCheck
        } else {
            return getBasinPoints(grid, pointsToCheck + adjacentBasinPoints)
        }

    }

    fun part2(input: List<List<Int>>) {
        val grid = Grid(input)
        val threeLargestBasinsProduct = getLowPoints(grid)
            .map { lowpoint -> getBasinPoints(grid, setOf(lowpoint)) }
            .map { it.size }
            .sortedBy { it }
            .takeLast(3)
            .fold(1) { acc, next -> acc * next }
        println("Day 9 part 2. Three largets basins size multiplied: $threeLargestBasinsProduct")

    }

    val testinput = readInput("day9/day09_test")
    val input = readInput("day9/day09")

    fun parseInput(data: List<String>): List<List<Int>> {
        return data.map { row ->  row.split("").filter { it.isNotEmpty() }.map { it.toInt() } }
    }

    part1(parseInput(input))
    part2(parseInput(input))


}

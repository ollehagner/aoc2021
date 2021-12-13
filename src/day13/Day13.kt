package day13

import common.Grid
import common.Point
import readInput

typealias Fold=Pair<String, Int>

fun main() {

    fun createFoldFunction(fold: Fold): (Point) -> Point {
        val axis = fold.first
        val value = fold.second
        if(axis == "x") {
            return { point -> if(point.x > value) Point(value - (point.x - value), point.y) else point }
        } else if(axis == "y") {
            return { point -> if(point.y > value) Point(point.x,value - (point.y - value)) else point }
        } else {
            throw IllegalArgumentException("Unknown axis: ${fold.first}")
        }
    }

    fun part1(points: List<Point>, fold: Fold) {
        val foldFunction = createFoldFunction(fold)
        val foldedPoints = points
            .map { foldFunction(it) }
            .toSet()
        println("Day 13 part 1. Num of dots after first fold: ${foldedPoints.size}")
    }

    fun part2(points: List<Point>, folds: List<Fold>) {
        val grid = Grid<String>()
        folds
            .map { createFoldFunction(it) }
            .fold(points) { acc, foldFunction ->
                acc.map { foldFunction(it) }
            }.forEach { grid.set(it, "#") }
        println("Day 13 part 2. Password is:")
        println(grid)
    }

//    val input = readInput("day13/day13_test")
    val input = readInput("day13/day13")

    val points = input
        .takeWhile { it.isNotEmpty() }
        .map { Point(it.substringBefore(",").toInt(), it.substringAfter(",").toInt()) }

    val folds = input
        .dropWhile { it.isNotEmpty() }
        .filter { it.isNotEmpty() }
        .map { it.substringAfterLast(" ") }
        .map { Pair(it.substringBefore("="), it.substringAfter("=").toInt()) }

    part1(points, folds.first())
    part2(points, folds)

}

package day5

import common.Point
import common.Vector
import readInput
import java.lang.Integer.min
import kotlin.math.max

fun main() {

    fun isHorizontalOrVertical(vector: Vector): Boolean {
        return (vector.from.x == vector.to.x) || (vector.from.y == vector.to.y)
    }

    fun allPointsInVector(vector: Vector): List<Point> {
        return if(isHorizontalOrVertical(vector)) {
            IntRange(min(vector.to.x, vector.from.x), max(vector.to.x, vector.from.x))
                    .flatMap { xValue ->
                        IntRange(min(vector.to.y, vector.from.y), max(vector.to.y, vector.from.y))
                                .map { yValue -> Point(xValue, yValue) }
                    }
        } else {
            val k = (vector.to.y - vector.from.y) / (vector.to.x - vector.from.x)
            val m = vector.from.y - (vector.from.x * k)
            IntRange(min(vector.to.x, vector.from.x), max(vector.to.x, vector.from.x))
                    .map { x -> Point(x, (k*x) + m) }
        }
    }

    fun parse(input: List<String>): List<Vector> {
        return input
                .map { it.split(" -> ") }
                .map { Vector(Point(it.first()), Point(it.last())) }
    }

    fun part1(points: List<Vector>) {

        val overlapCount = points
                .filter { isHorizontalOrVertical(it) }
                .flatMap { allPointsInVector(it) }
                .groupBy { it }
                .count { it.value.size >= 2}

        println("Day 5 part 1. Count is $overlapCount")

    }

    fun part2(points: List<Vector>) {
        val overlapCount = points
                .flatMap { allPointsInVector(it) }
                .groupBy { it }
                .count { it.value.size >= 2}

        println("Day 5 part 2. Count is $overlapCount")
    }

    val testInput = readInput("day5/day05_test")
    val input = readInput("day5/day05")

    part1(parse(input))
    part2(parse(input))

}

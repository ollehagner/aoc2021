package day17

import common.Point
import readInput
import java.lang.Math.abs

typealias TargetArea = Pair<IntRange, IntRange>

fun main() {

    fun gauss(value: Int): Int {
        return (value * value + value) / 2
    }

    fun infinite(startValue: Int): Sequence<Int> = sequence {
        var value = startValue
        while (true) {
            yield(value++)
        }
    }


    fun xSequence(startVelocity: Int, maxPosition: Int): Sequence<Int> {
        return sequence {
            var velocity = startVelocity
            var position = 0
            while (position <= maxPosition) {
                position += velocity
                yield(position)
                if (velocity > 0) {
                    velocity--
                } else if (velocity < 0) {
                    velocity++
                }

            }
        }
    }

    val ySequence = { startVelocity: Int, minPosition: Int ->
        sequence {
            var velocity = startVelocity
            var position = 0
            while (position >= minPosition) {
                position += velocity
                yield(position)
                velocity--
            }
        }
    }

    fun valuesInXSequence(range: IntRange): List<Int> {
        return infinite(0)
            .dropWhile { gauss(it) < range.first }
            .takeWhile { it <= range.last }
            .filter { xSequence(it, range.last).any { position -> position in range } }
            .toList()
    }

    fun valuesInYSequence(range: IntRange): List<Int> {
        return infinite(range.first)
            .takeWhile { it < abs(range.first) }
            .filter { ySequence(it, range.first).any { position -> position in range } }
            .toList()
    }


    fun part1(targetArea: TargetArea) {
        val maxY = valuesInYSequence(targetArea.second).maxOf { it }
        println("Day 17 part 1. Max height = ${gauss(maxY)}")
    }

    fun part2(targetArea: TargetArea) {
        val xValues = valuesInXSequence(targetArea.first)
        val yValues = valuesInYSequence(targetArea.second)
        val count = xValues.flatMap { xVelocity ->
            yValues.map { yVelocity -> Point(xVelocity, yVelocity) }
        }
            .filter { xSequence(it.x, targetArea.first.last).zip(ySequence(it.y, targetArea.second.first)).any { (x, y) -> targetArea.contains(Point(x, y)) } }
            .count()

        println("Day 17 part 2. Num of valid options = $count")
    }

    val testInput = "target area: x=20..30, y=-10..-5"
    val input = readInput("day17/day17").first()

    val ranges = ".*x=(-?\\d+)\\.\\.(-?\\d+).*y=(-?\\d+)\\.\\.(-?\\d+)".toRegex()
    val groups = ranges.matchEntire(input)!!.groupValues
    val xRange = IntRange(groups[1].toInt(), groups[2].toInt())
    val yRange = IntRange(groups[3].toInt(), groups[4].toInt())
    val targetArea = TargetArea(xRange, yRange)

    part1(targetArea)
    part2(targetArea)


}

fun TargetArea.contains(point: Point): Boolean {
    return point.x in this.first && point.y in this.second
}



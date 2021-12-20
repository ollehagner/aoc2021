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

    val ySequence = { startVelocity: Int ->
        sequence {
            var velocity = startVelocity
            var position = 0
            while (true) {
                position += velocity
                yield(position)
                velocity--
            }
        }
    }

    fun valuesInXSequence(range: IntRange): List<Int> {
        return infinite(0)
            .dropWhile { gauss(it) < range.first }
            .takeWhile { it < range.last }
            .filter { xSequence(it, range.last).any { position -> position in range } }
            .toList()
    }

    fun valuesInYSequence(range: IntRange): List<Int> {
        return infinite(0)
            .takeWhile { it < abs(range.first) }
            .filter { ySequence(it).any { position -> position in range } }
            .toList()
    }


    fun part1(targetArea: TargetArea) {
//        val xValues = valuesInXSequence(targetArea.first)
//        val yValues = valuesInYSequence(targetArea.second)
        val maxY = valuesInYSequence(targetArea.second).maxOf { it }
        println("Day 17 part 1. Max height = ${gauss(maxY)}")
//        val lastXValueInTargetArea = targetArea.first.last
//        xSequence(xValues.first(), lastXValueInTargetArea)
//            .zip(ySequence(3))
//            .map { (x, y) -> Point(x, y) }
//            .takeWhile { point -> point.x < targetArea.first.last && point.y > targetArea.second.last }
//            .forEach { println(it) }

    }

    fun part2(targetArea: TargetArea) {
        val xValues = valuesInXSequence(targetArea.first)
        val yValues = valuesInYSequence(targetArea.second)
        println(xValues)
        println(yValues)
        println("Size = ${xValues.size * yValues.size}")
    }

    val testInput = "target area: x=20..30, y=-10..-5"
    val input = readInput("day17/day17").first()

    val ranges = ".*x=(-?\\d+)\\.\\.(-?\\d+).*y=(-?\\d+)\\.\\.(-?\\d+)".toRegex()
    val groups = ranges.matchEntire(testInput)!!.groupValues
    val xRange = IntRange(groups[1].toInt(), groups[2].toInt())
    val yRange = IntRange(groups[3].toInt(), groups[4].toInt())
    val targetArea = TargetArea(xRange, yRange)

//    part1(targetArea)
    part2(targetArea)


}

fun TargetArea.contains(point: Point): Boolean {
    return point.x in this.first && point.y in this.second
}



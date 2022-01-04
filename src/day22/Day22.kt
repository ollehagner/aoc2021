package day22

import common.Grid3D
import common.Point3D
import readInput


fun main() {

    fun part1(input: List<String>) {
        val reboots = input.map { Reboot.of(it) }
        val grid = Grid3D<Boolean>()
        val validRange = IntRange(-50, 50)
        reboots
            .filter { it.cuboid.allWithin(validRange) }
            .forEach { reboot ->
                reboot.cuboid.toPoints().forEach { grid.set(it, reboot.on) }
            }

        val onCubes = grid.values().count { it }
        println("Day 22 part 1. Number of cubes switched on: $onCubes")
    }

    fun part2(input: List<String>) {
        val reboots = mutableListOf<Reboot>()
        input
            .map { Reboot.of(it) }
            .forEach { reboot ->
                val overlaps = reboots
                    .filter { it.cuboid.overlaps(reboot.cuboid) }
                    .map { Reboot(!it.on, it.cuboid.overlap(reboot.cuboid)) }
                    .toList()
                reboots.addAll(overlaps)
                if(reboot.on) {
                    reboots.add(reboot)
                }
            }

        val sum = reboots.sumOf { it.value() }
        println("Day 22 part 2. Number of cubes switched on: $sum")
    }

    val testinput = readInput("day22/day22_test_3")
    val input = readInput("day22/day22")
    part1(input)
    part2(input)
}

class Reboot(val on: Boolean, val cuboid: Cuboid) {

    companion object {
        fun of(line: String): Reboot {
            val on = line.startsWith("on")
            val xValues = line.substringAfter("x=").substringBefore(",y").split("..")
            val yValues = line.substringAfter("y=").substringBefore(",z").split("..")
            val zValues = line.substringAfter("z=").split("..")
            return Reboot(on, Cuboid(toRange(xValues), toRange(yValues), toRange(zValues)))
        }

        fun toRange(values: List<String>): IntRange {
            val lower = values.first().toInt()
            val upper = values.last().toInt()
            return IntRange(lower, upper)
        }

    }

    fun value(): Long {
        return if (on) cuboid.size() else -cuboid.size()
    }

    override fun toString(): String {
        return (if(on) "on" else "off") + " $cuboid)"
    }


}

class Cuboid(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {


    fun size(): Long {
        return xRange.count().toLong() * yRange.count().toLong() * zRange.count().toLong()
    }

    fun overlap(other: Cuboid): Cuboid {
        return Cuboid(
            overlapRange(xRange, other.xRange),
            overlapRange(yRange, other.yRange),
            overlapRange(zRange, other.zRange)
        )
    }

    private fun overlapRange(a: IntRange, b: IntRange): IntRange {
        return IntRange(maxOf(a.start, b.start), minOf(a.endInclusive, b.endInclusive))
    }

    fun overlaps(other: Cuboid): Boolean {
        return xRange.intersects(other.xRange) && yRange.intersects(other.yRange) && zRange.intersects(other.zRange)
//        return (xRange.contains(other.xRange.first) || xRange.contains(other.xRange.last)) &&
//                (yRange.contains(other.yRange.first) || yRange.contains(other.yRange.last)) &&
//                (zRange.contains(other.zRange.first) || zRange.contains(other.zRange.last))
    }

    fun allWithin(range: IntRange): Boolean {
        return range.contains(xRange.first) && range.contains(xRange.last) &&
                range.contains(yRange.first) && range.contains(yRange.last) &&
                range.contains(zRange.first) && range.contains(zRange.last)
    }

    fun toPoints(): List<Point3D> {
        return xRange.flatMap { x ->
            yRange.flatMap { y ->
                zRange.map { z ->
                    Point3D(x, y, z)
                }
            }
        }
    }

    override fun toString(): String {
        return "(x=$xRange, y=$yRange, z=$zRange)"
    }


}
infix fun IntRange.intersects(other: IntRange): Boolean =
    first <= other.last && last >= other.first

infix fun IntRange.intersect(other: IntRange): IntRange =
    maxOf(first, other.first)..minOf(last, other.last)

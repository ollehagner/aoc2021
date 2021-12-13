package day11


import common.Grid
import common.Point
import readInput

fun main() {

    fun iterate(grid: Grid<Octopus>) {
        val toCharge = grid.allPoints().toMutableList()
        while (toCharge.isNotEmpty()) {
            val point = toCharge.removeFirst()
            val octopus = grid.valueOf(point)
            val flashed = octopus.charge()
            if (flashed) {
                toCharge.addAll(point.surroundingPoints().filter { grid.hasValue(it) })
            }
        }
    }

    fun part1(grid: Grid<Octopus>) {
        val totalflashes = IntRange(1, 100)
            .fold(0) { flashCount, _ ->
                iterate(grid)
                val flashed = grid.values().filter { it.hasFlashed }
                flashed.forEach(Octopus::reset)
                flashCount +  flashed.size
            }
        println("Day 11 part 1. Number of flashes: $totalflashes")

    }

    fun part2(grid: Grid<Octopus>) {
        var allFlashed = false
        var iteration = 0L
        while(!allFlashed) {
            iterate(grid)
            val flashed = grid.values().filter { it.hasFlashed }
            flashed.forEach(Octopus::reset)
            allFlashed = flashed.size == grid.size()
            iteration++
        }
        println("Day 11 part 2. All flashed at iteration $iteration")
    }

    fun toGrid(input: List<String>): Grid<Octopus> {
        return Grid(input.map { row -> row.split("").filter { it.isNotEmpty()}.map { Octopus(it.toInt()) } })
    }

    val testinput = readInput("day11/day11_test")
    val input = readInput("day11/day11")

    part1(toGrid(input))
    part2(toGrid(input))

}

class Octopus(startingEnergy: Int) {

    var energyLevel = startingEnergy
    var hasFlashed = false

    fun reset() {
        hasFlashed = false
        energyLevel = 0
    }

    fun charge(): Boolean {
        energyLevel++
        if (energyLevel == 10) {
            hasFlashed = true
            return true
        } else {
            return false
        }
    }

    override fun toString(): String {
        return if (energyLevel < 10) "$energyLevel" else "9"
    }


}

fun Point.surroundingPoints(): Set<Point> {
    return IntRange(this.x - 1, this.x + 1)
        .flatMap { xValue ->
            IntRange(this.y - 1, this.y + 1)
                .map { yValue ->
                    Point(xValue, yValue)
                }
        }
        .filter { it != this }
        .toSet()
}

package day25

import common.Direction
import common.Grid
import common.Point
import readInput

fun main() {

    fun parseInput(path: String): Grid<Seafloor> {
        return Grid(readInput(path)
            .map { it.toCharArray().map { symbol -> Seafloor.of(symbol) } })
    }

    fun iterations(start: Grid<Seafloor>): Sequence<Pair<Int, Grid<Seafloor>>> {
        var grid = start
        return sequence {
            while (true) {
                var moves = 0
                val eastMoves = grid.entries()
                    .filter { entry -> entry.value == Seafloor.EAST }
                    .map { entry ->
                                val currentPoint = entry.key
                                val pointToCheck =
                                    if (grid.hasValue(currentPoint.move(Direction.RIGHT))) currentPoint.move(Direction.RIGHT) else Point(
                                        0,
                                        currentPoint.y
                                    )
                                if (grid.valueOf(pointToCheck) == Seafloor.EMPTY) {
                                    moves++
                                    Pair(currentPoint, pointToCheck)
                                } else {
                                    null
                                }
                    }
                    .filterNotNull()
                eastMoves.forEach { move ->
                    grid.set(move.first, Seafloor.EMPTY)
                    grid.set(move.second, Seafloor.EAST)
                }

                val southMoves = grid.entries()
                    .filter { entry -> entry.value == Seafloor.SOUTH }
                    .map { entry ->
                        val currentPoint = entry.key
                        val pointToCheck =
                            if (grid.hasValue(currentPoint.move(Direction.UP))) currentPoint.move(Direction.UP) else Point(
                                currentPoint.x,
                                0
                            )
                        if (grid.valueOf(pointToCheck) == Seafloor.EMPTY) {
                            moves++
                            Pair(currentPoint, pointToCheck)
                        } else {
                            null
                        }
                    }
                    .filterNotNull()
                southMoves.forEach { move ->
                    grid.set(move.first, Seafloor.EMPTY)
                    grid.set(move.second, Seafloor.SOUTH)
                }
                yield(Pair(moves, Grid(grid.rows())))
            }
        }

    }

    fun part1(grid: Grid<Seafloor>) {
        val iterations = iterations(grid)
            .takeWhile { it.first > 0 }
            .count()
        println("Day 25 part 1. Stopped moving after ${iterations + 1} iterations")
    }

    val grid = parseInput("day25/day25")
    part1(grid)
}

enum class Seafloor(private val symbol: Char) {
    EAST('>'), SOUTH('v'), EMPTY('.');

    companion object {
        fun of(symbol: Char): Seafloor {
            return when (symbol) {
                '>' -> EAST
                'v' -> SOUTH
                '.' -> EMPTY
                else -> throw IllegalArgumentException("Invalid value $symbol")
            }
        }
    }

    override fun toString(): String {
        return "$symbol"
    }

}

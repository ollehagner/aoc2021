package day20

import common.Grid
import common.Point
import readInput


fun main() {

    fun adjacent(point: Point): List<Point> {
        return (-1..1).flatMap { y ->
            (-1..1).map { x ->
                Point(point.x + x, point.y + y)
            }
        }
            .toList()
    }

    fun pointsToProcess(min: Point, max: Point): List<Point> {
        return IntRange(min.y - 1, max.y + 1).flatMap { y ->
            IntRange(min.x - 1, max.x + 1).map { x ->
                Point(x, y)
            }
        }.toList()
    }

    fun enhance(grid: Grid<Pixel>, algorithm: Array<Pixel>): Sequence<Int> {
        return sequence {
            var currentGrid = grid
            var defaultValue = Pixel.DARK
            while(true) {

                currentGrid = pointsToProcess(currentGrid.min(), currentGrid.max())
                    .map {  point ->
                        val algorithmIndex = adjacent(point)
                            .map { currentGrid.valueOrDefault(it, defaultValue)}
                            .map { it.value }
                            .joinToString("").toInt(2)
                        Pair(point, algorithm[algorithmIndex])
                    }.fold(Grid()) {
                        acc, value -> acc.set(value.first, value.second)
                        acc
                    }
                defaultValue = algorithm["${defaultValue.value}".repeat(9).toInt(2)]
                yield(currentGrid.values().count { it == Pixel.LIGHT })
            }

        }
    }

    fun part1(grid: Grid<Pixel>, algorithm: Array<Pixel>) {
        val litPixels = enhance(grid, algorithm).take(2).last()
        println("Day 20 part 1. Num of lit pixels : $litPixels")
    }

    fun part2(grid: Grid<Pixel>, algorithm: Array<Pixel>) {
        val litPixels = enhance(grid, algorithm).take(50).last()
        println("Day 20 part 2. Num of lit pixels : $litPixels")
    }
//    val input = readInput("day20/day20_test")
    val input = readInput("day20/day20")

    val algorithm = input.first()
        .toCharArray().map { Pixel.of(it) }.toTypedArray()

    val grid = Grid(input.drop(2)
        .map { row ->
            row.map { Pixel.of(it) }
        })

    part1(grid, algorithm)
    part2(grid, algorithm)
}

enum class Pixel(val symbol: Char, val value: Int) {
    LIGHT('#', 1), DARK('.', 0);

    companion object {
        fun of(symbol: Char): Pixel {
            return when (symbol) {
                '#' -> LIGHT
                '.' -> DARK
                else -> throw IllegalArgumentException("Invalid value $symbol")
            }
        }
    }

    override fun toString(): String {
        return "$symbol"
    }


}

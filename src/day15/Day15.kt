package day15

import common.Direction.*
import common.Grid
import common.Point
import readInput
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

fun main() {

    fun parseInputToGrid(input: List<String>): Grid<Int> {
        return Grid(input
            .map {
                it.split("")
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }
            })
    }

    fun adjacent(point: Point): List<Point> {
        return listOf(
            point.move(UP, 1),
            point.move(DOWN, 1),
            point.move(LEFT, 1),
            point.move(RIGHT, 1)
        )
    }

    fun solve(grid: Grid<Int>): Int {
        val startpoint = grid.min()
        val endpoint = grid.max()

        val firstPath = Path(startpoint, endpoint)
        firstPath.addToPath(startpoint, 0)
        val pathsToExplore = mutableListOf(firstPath)
        val finished = mutableListOf<Int>()

        val cheapestRoute = mutableMapOf<Point, Int>()

        while (pathsToExplore.isNotEmpty()) {
            val currentPath = pathsToExplore.removeFirst()
            if (currentPath.isCompleted()) {
                finished.add(currentPath.cost())
            } else {
                adjacent(currentPath.currentPoint())
                    .filter { grid.hasValue(it) && !currentPath.contains(it) }
                    .forEach { nextPoint ->
                        val newPath = currentPath.copy()
                        newPath.addToPath(nextPoint, grid.valueOf(nextPoint))
                        if (newPath.cost() < cheapestRoute.getOrDefault(nextPoint, Int.MAX_VALUE)) {
                            cheapestRoute[nextPoint] = newPath.cost()
                            pathsToExplore.removeAll { it.currentPoint() == nextPoint }
                            pathsToExplore.add(newPath)
                        }
                    }
            }
        }
        return finished.minOf { it }
    }

    fun wrapAround(value: Int, increment: Int): Int {
        val sum = value + increment
        return if (sum >= 10) sum - 9 else sum
    }

    fun expandEntry(startPoint: Point, startValue: Int, offset: Int): List<Pair<Point, Int>> {
        return IntRange(0, 4).flatMap { y ->
            IntRange(0, 4).map { x ->
                Point(startPoint.x + x * offset, startPoint.y + y * offset)
            }
        }
        .scan(Pair(startPoint, startValue - 1)) { previous, point ->
            val value = if (previous.first.y == point.y) wrapAround(previous.second, 1) else
                wrapAround(previous.second, 6)
                Pair(point, value)
            }
            .toList()
    }

    fun expand(grid: Grid<Int>): Grid<Int> {
        return grid.entries()
            .flatMap { entry -> expandEntry(entry.key, entry.value, grid.rows().size) }
            .fold(Grid()) { expandedGrid, entry ->
                expandedGrid.set(entry.first, entry.second)
                expandedGrid
            }
    }

    fun part1(input: List<String>) {
        val grid = parseInputToGrid(input)
        val cheapestPath = solve(grid)
        println("Day 15 part 1. Minimum risk: $cheapestPath")
    }

    fun part2(input: List<String>) {
        val grid = expand(parseInputToGrid(input))
        val cheapestPath = solve(grid)
        println("Day 15 part 2. Minimum risk: $cheapestPath")
    }

    val testinput = readInput("day15/day15_test")
    val testinputPart2 = readInput("day15/day15_test_part2")
    val input = readInput("day15/day15")

//    part1(input)
    part2(input)


}

data class Path(
    val start: Point,
    val end: Point,
    val visited: LinkedList<Point> = LinkedList(),
    val cost: AtomicInteger = AtomicInteger()
) {

    fun copy(): Path {
        return Path(
            start,
            end,
            visited = LinkedList(visited),
            cost = AtomicInteger(this.cost.get())
        )
    }

    fun cost(): Int {
        return cost.get()
    }

    fun addToPath(point: Point, cost: Int) {
        visited.addLast(point)
        this.cost.addAndGet(cost)
    }

    fun contains(point: Point): Boolean {
        return visited.contains(point)
    }

    fun currentPoint(): Point {
        return visited.last
    }

    fun isCompleted(): Boolean {
        return visited.last == end
    }

    override fun toString(): String {
        return "Path(visited=$visited, cost=${cost.get()})"
    }


}

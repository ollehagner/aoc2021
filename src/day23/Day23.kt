package day23

import common.Direction.*
import common.Grid
import common.Point
import day23.Amphipod.*
import day23.Structure.*
import readInput
import java.lang.Math.abs
import java.util.*

fun parseBurrow(input: List<String>): Burrow {
    val gridData = input
        .map { it.toCharArray() }
        .map { line -> line.map { Square.of(it) }.toList() }
    val grid = Grid(gridData)
    val burrowLayout = BurrowLayout(grid)
    return Burrow(grid, 0, burrowLayout)
}

fun main() {
    fun solve(start: Burrow): Burrow {

        var handledStates = HashMap<Int, Int>()
        val queue = PriorityQueue<Burrow>(compareBy { it.totalEnergy + it.score })
        queue.add(start)
        var lowestCostBurrow = Burrow(Grid(), Int.MAX_VALUE, start.burrowLayout)
        var counter = 0
        while (queue.isNotEmpty()) {
            counter++

            val currentBurrow = queue.poll()
//            println("Energy: " + currentBurrow.totalEnergy)
//            println("Score: " + currentBurrow.score)
//            println(currentBurrow)
            if(currentBurrow.isComplete()) return currentBurrow

            handledStates.merge(
                currentBurrow.toString().hashCode(),
                currentBurrow.totalEnergy
            ) { oldValue, newValue -> if (newValue < oldValue) newValue else oldValue }
            val newStates = currentBurrow.allPossibleMoves().map { currentBurrow.move(it) }
            queue.removeIf { it.totalEnergy > currentBurrow.totalEnergy + currentBurrow.score }

            newStates
                .filter { newState ->
                    newState.totalEnergy < handledStates.getOrDefault(newState.toString().hashCode(), Int.MAX_VALUE)
                }
                .forEach { queue.offer(it) }
        }

        return lowestCostBurrow
    }

    fun part1(start: Burrow) {
        val startTime = System.currentTimeMillis()
        val lowestCostBurrow = solve(start)
        lowestCostBurrow.history.forEach { println(it) }
        println("Day 23 part 1. Lowest cost: ${lowestCostBurrow.totalEnergy}")
        println("Finished in ${System.currentTimeMillis() - startTime} ms")
    }

    fun part2(start: Burrow) {
        val startTime = System.currentTimeMillis()
        val lowestCostBurrow = solve(start)
        println("Day 23 part 2. Lowest cost: ${lowestCostBurrow.totalEnergy}")
        println("Finished in ${System.currentTimeMillis() - startTime} ms")
    }


    val testinputPart1 = readInput("day23/day23_test")
    val testinputPart2 = readInput("day23/day23_part2_test")
    val inputPart1 = readInput("day23/day23")
    val inputPart2 = readInput("day23/day23_part2")

    val burrow1 = parseBurrow(inputPart1)
    val burrow2 = parseBurrow(inputPart2)

    part1(burrow1)
    part2(burrow2)
}

class BurrowLayout(grid: Grid<Square>) {
    val AMBER_HOME = Point.sequence(Point(3, 2), UP).takeWhile { grid.valueOf(it) != WALL }.toList()
    val BRONZE_HOME = Point.sequence(Point(5, 2), UP).takeWhile { grid.valueOf(it) != WALL }.toList()
    val COPPER_HOME = Point.sequence(Point(7, 2), UP).takeWhile { grid.valueOf(it) != WALL }.toList()
    val DESERT_HOME = Point.sequence(Point(9, 2), UP).takeWhile { grid.valueOf(it) != WALL }.toList()

    val OUTSIDE_ROOMS = listOf(Point(3, 1), Point(5, 1), Point(7, 1), Point(9, 1))
}

class Burrow(val grid: Grid<Square>, val totalEnergy: Int, val burrowLayout: BurrowLayout, val history: List<String> = emptyList()) {

    val score = amphipods()
        .map { (point, amphipod) ->
            val home = home(amphipod)
            if (point in home) {
                if(atFinalDestination(point, amphipod)) {
                    0
                } else {
                    (point.y - 1) * 2
                }
            } else {
                val toHallwayCost = Point.sequence(point, DOWN)
                    .takeWhile { it.y >= 1 }
                    .filter { it != point }
                    .count()
                val horizontalDirection = if (point.x < home.last().x) RIGHT else LEFT
                val horizontalCost = Point.sequence(Point(point.x, 1), horizontalDirection)
                    .drop(1)
                    .takeWhile { it != Point(home.last().x, 1) }
                    .count()
                val toDestinationCost = 2
                (toHallwayCost + horizontalCost + toDestinationCost) * amphipod.energy
            }
        }
        .sum()

    fun toDestination(point: Point, amphipod: Amphipod): Set<Point> {
        val home = home(amphipod)
        val toHallway = Point.sequence(Point(point.x, point.y - 1), DOWN).takeWhile { it.y >= 1 }
            .filter { it != point }
            .toList()
        val horizontalDirection = if (point.x < home.last().x) RIGHT else LEFT
        val toOutsideHome = Point.sequence(Point(point.x, 1), horizontalDirection).takeWhile { it.x != home.first().x }
            .filter { it != point }
            .toList()
        val verticalToDestination = Point.sequence(home.last(), DOWN)
            .dropWhile { grid.valueOf(it) == amphipod }
            .takeWhile { it.y >= 1 }.toList().reversed()
        return buildSet {
            addAll(toHallway)
            addAll(toOutsideHome)
            addAll(verticalToDestination)
        }
    }

    fun amphipods(): List<Pair<Point, Amphipod>> {
        return grid.entries()
            .filter { it.value is Amphipod }
            .map { Pair(it.key, it.value as Amphipod) }
    }

    fun move(move: Move): Burrow {
        val amphipodToMove = (grid.valueOf(move.from)) as Amphipod
        val copy = Grid(grid.rowsWithDefault(NOT_OCCUPIED))
        copy.set(move.to, amphipodToMove)
        copy.set(move.from, NOT_OCCUPIED)
        val energy = move.steps * amphipodToMove.energy
        return Burrow(copy, totalEnergy + energy, burrowLayout, history.plus(grid.toString() + "Energy $energy\n"))
    }

    fun home(amphipod: Amphipod): List<Point> {
        return when (amphipod) {
            AMBER -> burrowLayout.AMBER_HOME
            BRONZE -> burrowLayout.BRONZE_HOME
            COPPER -> burrowLayout.COPPER_HOME
            DESERT -> burrowLayout.DESERT_HOME
        }
    }

    fun inHallway(point: Point): Boolean {
        return point.y == 1
    }

    fun isComplete(): Boolean {
        return amphipods()
            .all { it.first in home(it.second) }
    }

    fun allPossibleMoves(): List<Move> {
        return amphipods()
            .flatMap { pair ->
                validMoves(pair.first, pair.second)
            }
    }

    fun validMoves(currentPoint: Point, amphipod: Amphipod): Set<Move> {

        if (atFinalDestination(currentPoint, amphipod)) {
            return emptySet()
        }

        val pathToDestination = toDestination(currentPoint, amphipod)
        if (pathToDestination.all { grid.valueOf(it) == NOT_OCCUPIED }) {
            return setOf(Move(currentPoint, pathToDestination.last(), pathToDestination.count()))
        }

        if(!inHallway(currentPoint) && IntRange(1, currentPoint.y - 1).all { grid.valueOf(Point(currentPoint.x, it)) == NOT_OCCUPIED}) {
            val leftMoves = IntRange(1, currentPoint.x - 1).reversed()
                .map { Point(it, 1) }
                .takeWhile { grid.valueOf(it) == NOT_OCCUPIED }
                .filter { it !in burrowLayout.OUTSIDE_ROOMS }
            val rightMoves = IntRange(currentPoint.x + 1, 11)
                .map { Point(it, 1) }
                .takeWhile { grid.valueOf(it) == NOT_OCCUPIED }
                .filter { it !in burrowLayout.OUTSIDE_ROOMS }
            return listOf(leftMoves, rightMoves).flatten()
                .map { to -> Move(currentPoint, to, (abs(currentPoint.x - to.x) + abs(currentPoint.y - to.y))) }
                .toSet()
        }
        return emptySet()
    }

    fun atFinalDestination(point: Point, amphipod: Amphipod): Boolean {
        val pointsBelow = IntRange(point.y + 1, home(amphipod).last().y).map { Point(point.x, it) }
        return point in home(amphipod) && pointsBelow.all { grid.valueOf(it) == amphipod }
    }

    fun print(point: Point) {
        println(grid.valueOrDefault(point, INVALID_SPACE))
    }

    override fun toString(): String {
        return grid.toString()
    }


}

data class Move(val from: Point, val to: Point, val steps: Int) {
}

interface Square {

    companion object {
        fun of(value: Char): Square {
            return when (value) {
                '#' -> WALL
                '.' -> NOT_OCCUPIED
                'A' -> AMBER
                'B' -> BRONZE
                'C' -> COPPER
                'D' -> DESERT
                else -> INVALID_SPACE
            }
        }
    }

}

enum class Structure(val sign: Char) : Square {
    WALL('#'), NOT_OCCUPIED('.'), INVALID_SPACE(' ');

    override fun toString(): String {
        return "$sign"
    }
}

enum class Amphipod(val sign: Char, val energy: Int) : Square {
    AMBER('A', 1), BRONZE('B', 10), COPPER('C', 100), DESERT('D', 1000);

    override fun toString(): String {
        return "$sign"
    }

}

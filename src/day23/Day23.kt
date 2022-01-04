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
    return Burrow(input.map { it.toCharArray() }
        .map { line -> line.map { Square.of(it) }.toList() }, 0)
}

fun main() {

    fun part1(start: Burrow) {
        val handledStates = HashMap<String, Int>()
        val queue = PriorityQueue<Burrow>(compareBy { -it.score })
        queue.add(start)
        var lowestCostBurrow = Burrow(emptyList(), Int.MAX_VALUE)
        while(queue.isNotEmpty()) {
            val currentBurrow = queue.poll()
            handledStates.merge(currentBurrow.toString(), currentBurrow.totalEnergy) { oldValue, newValue -> if(newValue < oldValue) newValue else oldValue}
            val possibleMoves = currentBurrow.allPossibleMoves()
            possibleMoves
                .map { currentBurrow.move(it) }
                .filter { newState -> newState.totalEnergy < handledStates.getOrDefault(newState.toString(), Int.MAX_VALUE) }
                .forEach { queue.offer(it) }

            val possibleLowestCostBurrow = queue.filter { it.isComplete() }
                .minByOrNull { it.totalEnergy }
            if(possibleLowestCostBurrow != null && possibleLowestCostBurrow.totalEnergy < lowestCostBurrow.totalEnergy) {
                lowestCostBurrow = possibleLowestCostBurrow!!
            }
            queue.removeIf { it.isComplete() || it.totalEnergy >= lowestCostBurrow.totalEnergy}
        }
        println("Day 23 part 1. Lowest cost: ${lowestCostBurrow.totalEnergy}")
        lowestCostBurrow.history.forEach { println(it) }
        println(lowestCostBurrow)
    }

    val testinput = readInput("day23/day23_test")
    val input = readInput("day23/day23")
    val burrow = parseBurrow(input)

    part1(burrow)
}



class Burrow(values: List<List<Square>>, val totalEnergy: Int, val history: List<String> = emptyList()) {
    val grid = Grid(values)

    val AMBER_HOME = listOf(Point(3,2), Point(3,3))
    val BRONZE_HOME = listOf(Point(5,2), Point(5,3))
    val COPPER_HOME = listOf(Point(7,2), Point(7,3))
    val DESERT_HOME = listOf(Point(9,2), Point(9,3))

    val OUTSIDE_ROOMS = listOf(Point(3,1), Point(5,1), Point(7,1), Point(9,1))

    val score = grid.entries()
        .filter { it.value is Amphipod }
        .map { amphipodEntry ->
            when {
                inHallway(amphipodEntry.key) -> 1
                atFinalDestination(amphipodEntry.key) -> 10
                else -> 0
            }
        }
        .sum()

    fun move(move: Move):Burrow {
        val amphipodToMove = (grid.valueOf(move.from)) as Amphipod
        val copy = Grid(grid.rowsWithDefault(NOT_OCCUPIED))
        copy.set(move.to, amphipodToMove)
        copy.set(move.from, NOT_OCCUPIED)
        val energy = move.steps() * amphipodToMove.energy
        return  Burrow(copy.rowsWithDefault(NOT_OCCUPIED), totalEnergy + energy, history + this.toString())
    }

    fun home(amphipod: Amphipod): List<Point> {
        return when(amphipod) {
            AMBER -> AMBER_HOME
            BRONZE -> BRONZE_HOME
            COPPER -> COPPER_HOME
            DESERT -> DESERT_HOME
        }
    }

    fun inHallway(point: Point): Boolean {
        return point.y == 1
    }

    fun isComplete(): Boolean {
        return grid.entries()
            .filter { it.value is Amphipod }
            .all { it.key in home(it.value as Amphipod)}
    }

    fun allPossibleMoves(): List<Move> {
        return grid.entries()
            .filter { it.value is Amphipod }
            .flatMap { amphipodEntry ->
                validMovePoints(amphipodEntry.key, amphipodEntry.value as Amphipod)
                    .map { destination -> Move(amphipodEntry.key, destination)}
            }
    }

    fun validMovePoints(currentPoint: Point, amphipod: Amphipod): Set<Point> {
        if(atFinalDestination(currentPoint)) {
            return emptySet()
        }
//        println("Not at destination")
        val home = home(amphipod)
        if(currentPoint in home && grid.valueOf(home.last()) == NOT_OCCUPIED) {
            return setOf(home.last())
        }

//        println("Not at home")
        if(inHallway(currentPoint)) {
            val pathToHome = IntRange(minOf(currentPoint.x, home.first().x), maxOf(currentPoint.x, home.first().x))
                .map { Point(it, 1)}
                .filter { it != currentPoint }
            return if(pathToHome.all { grid.valueOf(it) == NOT_OCCUPIED }
                && grid.valueOf(home.last()) in listOf(amphipod, NOT_OCCUPIED)
                && grid.valueOf(home.first()) == NOT_OCCUPIED) {
                setOf(home.first())
            } else {
                emptySet()
            }
        }
//        println("Not in hallway")

        if(grid.valueOf(currentPoint.move(DOWN)) != NOT_OCCUPIED) {
            return emptySet()
        }
//        println("In wrong home, but not blocked")

        val leftMoves = IntRange(1, currentPoint.x - 1).reversed()
            .map { Point(it, 1)}
            .takeWhile { grid.valueOf(it) == NOT_OCCUPIED }
            .filter { it !in OUTSIDE_ROOMS }
        val rightMoves = IntRange(currentPoint.x + 1, 11)
            .map { Point(it, 1)}
            .takeWhile { grid.valueOf(it) == NOT_OCCUPIED }
            .filter { it !in OUTSIDE_ROOMS }
        return listOf(leftMoves, rightMoves).flatten().toSet()

    }

    fun atFinalDestination(point: Point): Boolean {
        val amphipod = grid.valueOf(point)
        if(amphipod !is Amphipod) {
            throw IllegalArgumentException("Point $point doesn't contain an amphipod, it contains $amphipod")
        }

        return (point == home(amphipod).last() || (point == home(amphipod).first() && grid.valueOf(home(amphipod).last()) == amphipod))
    }

    fun print(point: Point) {
        println(grid.valueOrDefault(point, INVALID_SPACE))
    }

    override fun toString(): String {
        return grid.toString()
    }


}

data class Move(val from: Point, val to: Point){
    fun steps(): Int { return abs(from.x - to.x) + abs(from.y - to.y) }
}

interface Square {

    companion object {
        fun of(value: Char): Square {
            return when(value) {
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

enum class Structure(val sign: Char): Square {
    WALL('#'), NOT_OCCUPIED('.'), INVALID_SPACE(' ');

    override fun toString(): String {
        return "$sign"
    }


}

enum class Amphipod(val sign: Char, val energy: Int): Square {
    AMBER('A',1), BRONZE('B',10), COPPER('C',100), DESERT('D',1000);

    override fun toString(): String {
        return "$sign"
    }

}

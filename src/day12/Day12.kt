package day12

import readInput
import java.util.*
import kotlin.collections.HashSet


fun main() {
    fun findDistinctPaths(caves: Set<Cave>, validPathPredicate: (List<Cave>) -> Boolean): Set<LinkedList<Cave>> {
        val firstPath = LinkedList<Cave>().apply { add(caves.first { it.name == "start" }) }
        val pathsToExplore = LinkedList<LinkedList<Cave>>().apply { add(firstPath) }
        val distinctPaths = mutableSetOf<LinkedList<Cave>>()

        while(pathsToExplore.isNotEmpty()) {
            val path = pathsToExplore.pop()!!
//            pathsToExplore.forEach { println(it)}
//            println("--------------------------")
            if(path.last.isEnd()) {
                distinctPaths.add(path)
            } else {
                path.last().connections
                    .map { connection ->
                       LinkedList<Cave>().apply {
                            addAll(path)
                            addLast(connection)
                        }
                    }
                    .filter { validPathPredicate(it) }
                    .forEach {
                        pathsToExplore.add(it)
                    }
            }

        }
        return distinctPaths
    }


    fun part1(caves: Set<Cave>) {
        val validPathPredicate = { path: List<Cave> ->
                path.groupingBy { it }
                    .eachCount()
                    .filter { (cave, _) -> cave.isSmallCave() }
                    .all { (_, count) ->  count < 2} }
        val distinctPaths = findDistinctPaths(caves, validPathPredicate)
        println("Day 12 part 1. Distinct paths: ${distinctPaths.size}")

    }

    fun part2(caves: Set<Cave>) {
        val validPathPredicate = { path: List<Cave> ->
            val smallCavesVisitedMoreThanOnce = path.groupingBy { it }
                .eachCount()
                .filter { (cave, visits) -> cave.isSmallCave() && visits >= 2 }
             smallCavesVisitedMoreThanOnce.size <= 1 && smallCavesVisitedMoreThanOnce.values.sum() <= 2 && smallCavesVisitedMoreThanOnce.keys.none { it.name == "start" }
        }
        val distinctPaths = findDistinctPaths(caves, validPathPredicate)
        println("Day 12 part 2. Distinct paths: ${distinctPaths.size}")

    }

    fun parseCaveSystem(input: List<String>): Set<Cave> {
        val caveMap = mutableMapOf<String, Cave>()

        input
            .flatMap { it.split("-") }
            .distinct()
            .forEach() { name -> caveMap[name] = Cave(name) }

        input
            .map { Pair(it.substringBefore("-"), it.substringAfter("-"))}
            .forEach { connection ->
                val firstCave = caveMap[connection.first]!!
                val secondCave = caveMap[connection.second]!!

                firstCave.addConnection(secondCave)
                secondCave.addConnection(firstCave)
            }
        return caveMap.values.toSet()
    }

    val testinput = readInput("day12/day12_test")
    val input = readInput("day12/day12")

    part1(parseCaveSystem(input))
    part2(parseCaveSystem(input))

}

class Cave(val name: String) {
    val connections = HashSet<Cave>()

    fun addConnection(node: Cave) {
        connections.add(node)
    }

    fun getconnections(): Set<Cave> {
        return connections.toSet()
    }

    fun isBigCave(): Boolean {
        return name.uppercase() == name
    }

    fun isSmallCave(): Boolean {
        return name.uppercase() != name
    }

    fun isEnd(): Boolean {
        return name == "end"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cave

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "$name"
    }


}

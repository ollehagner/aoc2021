package day7

import readInputAsListOfInts
import kotlin.math.abs

fun main() {

    fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize(this)

    fun part1(input: List<Int>) {
        val cheapestPosition = IntRange(input.minOf { it }, input.maxOf { it })
            .map { position ->
                Pair(position, input
                    .map { abs(position - it) }
                    .sum())
            }
            .minByOrNull { it.second }

        println("Day 7 part 1. Most fuel efficient position: ${cheapestPosition?.first} with consumption: ${cheapestPosition?.second}")
    }

    val sumFunction = { value: Int -> IntRange(1, value).sum() }.memoize()

    val gaussSum = { value: Int -> ((value*value) + value) / 2 }//.memoize()

    fun part2(input: List<Int>) {
        val cheapestPosition = IntRange(input.minOf { it }, input.maxOf { it })
            .map { position ->
                Pair(position, input
                    .map { gaussSum(abs(position - it)) }
                    .sum())
            }
            .minByOrNull { it.second }
        println("Day 7 part 2. Most fuel efficient position: ${cheapestPosition?.first} with consumption: ${cheapestPosition?.second}")
    }

    val testInput = listOf(16,1,2,0,4,2,7,1,2,14)
    val input = readInputAsListOfInts("day7/day07")
    part1(input)
    val start = System.currentTimeMillis()
    part2(input)
    println("Part 2 took ${System.currentTimeMillis() - start} ms")
}

private class Memoize<in T, out R>(val func: (T) -> R) : (T) -> R {
    private val cache = mutableMapOf<T, R>()

    override fun invoke(value: T): R {
        return cache.getOrPut(value) { func(value) }
    }
}

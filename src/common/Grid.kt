package common

import java.util.*


class Grid<T> {


    val data = mutableMapOf<Point, T>()

    constructor() {

    }

    constructor(values: List<List<T>>) {
        values.forEachIndexed { y, row ->
            row.forEachIndexed { x, value ->
                data[Point(x, y)] = value
            }
        }
    }

    fun allPoints() : Set<Point> {
        return data.keys.toSet()
    }

    fun values(): MutableCollection<T> {
        return data.values
    }

    fun valueOf(point: Point): T {
        return data[point]!!
    }

    fun valueOrDefault(point: Point, defaultValue: T): T {
        return data.getOrDefault(point, defaultValue)
    }

    fun maybeValue(point: Point): T? {
        return data[point]
    }

    fun hasValue(point: Point): Boolean {
        return data.containsKey(point)
    }

    fun valueOfExistingPoints(points: List<Point>) : List<T> {
        return points
            .map { maybeValue(it) }
            .filterNotNull()
    }

    fun set(point: Point, value: T) {
        data[point] = value
    }

    fun rows(): List<List<T>> {
        val min = min()
        val max = max()
        return IntRange(min.y, max.y)
                .map { y ->
                    IntRange(min.x, max.x)
                            .map { x -> valueOf(Point(x, y)) }

                }
    }

    fun columns(): List<List<T>> {
        val min = min()
        val max = max()
        return IntRange(min.x, max.x)
                .map { x ->
                    IntRange(min.y, max.y)
                            .map { y -> valueOf(Point(x, y)) }

                }
    }

    fun min(): Point {
        val minY = Optional.ofNullable(data.keys.map { it.y }.minOf { it }).orElse(0)
        val minX = Optional.ofNullable(data.keys.map { it.x }.minOf { it }).orElse(0)
        return Point(minX, minY)
    }

    fun max(): Point {
        val maxX = Optional.ofNullable(data.keys.map { it.x }.maxOf { it }).orElse(0)
        val maxY = Optional.ofNullable(data.keys.map { it.y }.maxOf { it }).orElse(0)
        return Point(maxX, maxY)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        val minY = Optional.ofNullable(data.keys.map { it.y }.minOf { it }).orElse(0)
        val minX = Optional.ofNullable(data.keys.map { it.x }.minOf { it }).orElse(0)
        val maxX = Optional.ofNullable(data.keys.map { it.x }.maxOf { it }).orElse(0)
        val maxY = Optional.ofNullable(data.keys.map { it.y }.maxOf { it }).orElse(0)
        (minY)!!.rangeTo(maxY!!).forEach { y ->
            (minX)!!.rangeTo(maxX!!).forEach { x ->
                sb.append(valueOf(Point(x, y)).toString())
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}

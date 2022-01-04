package common

import java.util.*


class Grid3D<T> {


    val data = mutableMapOf<Point3D, T>()

    constructor() {

    }

    fun size(): Int {
        return data.size
    }

    fun entries(): Set<Map.Entry<Point3D, T>> {
        return data.entries
    }

    fun allPoints() : Set<Point3D> {
        return data.keys.toSet()
    }

    fun values(): MutableCollection<T> {
        return data.values
    }

    fun valueOf(point: Point3D): T {
        return data[point]!!
    }

    fun valueOrDefault(point: Point3D, defaultValue: T): T {
        return data.getOrDefault(point, defaultValue)
    }

    fun maybeValue(point: Point3D): T? {
        return data[point]
    }

    fun hasValue(point: Point3D): Boolean {
        return data.containsKey(point)
    }

    fun valueOfExistingPoint3Ds(points: List<Point3D>) : List<T> {
        return points
            .map { maybeValue(it) }
            .filterNotNull()
    }

    fun set(point: Point3D, value: T) {
        data[point] = value
    }

    fun min(): Point3D {
        val minZ = Optional.ofNullable(data.keys.map { it.z }.minOf { it }).orElse(0)
        val minY = Optional.ofNullable(data.keys.map { it.y }.minOf { it }).orElse(0)
        val minX = Optional.ofNullable(data.keys.map { it.x }.minOf { it }).orElse(0)
        return Point3D(minX, minY, minZ)
    }

    fun max(): Point3D {
        val maxX = Optional.ofNullable(data.keys.map { it.x }.maxOf { it }).orElse(0)
        val maxY = Optional.ofNullable(data.keys.map { it.y }.maxOf { it }).orElse(0)
        val maxZ = Optional.ofNullable(data.keys.map { it.z }.maxOf { it }).orElse(0)
        return Point3D(maxX, maxY, maxZ)
    }


}

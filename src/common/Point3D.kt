package common

import kotlin.math.abs


class Point3D(val x: Int, val y: Int, val z: Int) {

    fun move(vector: Point3D): Point3D {
        return Point3D(this.x + vector.x, this.y + vector.y, this.z + vector.z)
    }

    fun relativeTo(other: Point3D): Point3D {
        return Point3D(other.x - this.x, other.y - this.y, other.z - this.z)
    }


    fun rotateRoundZAxis(): Point3D {
        return Point3D(this.y * -1, this.x, this.z)
    }

    fun rotateRoundYAxis(): Point3D {
        return Point3D(this.z, this.y, this.x * -1)
    }

    fun rotateRoundXAxis(): Point3D {
        return Point3D(this.x, this.z * -1, this.y)
    }

    fun manhattanDistance(other: Point3D): Int {
        return abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point3D

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

}

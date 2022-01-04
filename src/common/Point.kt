package common

data class Point(val x: Int, val y: Int) {
    constructor(values: String) :
        this(values.split(",").first().toInt(), values.split(",").last().toInt())

    fun move(direction: Direction, steps: Int = 1) : Point{
        return when(direction) {
            Direction.UP -> Point(x, y + steps)
            Direction.DOWN -> Point(x, y - steps)
            Direction.LEFT -> Point(x - steps, y)
            Direction.RIGHT -> Point(x + steps, y)
        }
    }

    fun move(vector: Point): Point {
        return Point(this.x + vector.x, this.y + vector.y)
    }

    fun relativeTo(other: Point): Point {
        return Point(other.x - this.x, other.y - this.y)
    }

    override fun toString(): String {
        return "($x,$y)"
    }

}

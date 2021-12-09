package common

data class Point(val x: Int, val y: Int) {
    constructor(values: String) :
        this(values.split(",").first().toInt(), values.split(",").last().toInt())

    fun move(direction: Direction, steps: Int) : Point{
        return when(direction) {
            Direction.UP -> Point(x, y + steps)
            Direction.DOWN -> Point(x, y - steps)
            Direction.LEFT -> Point(x - steps, y)
            Direction.RIGHT -> Point(x + steps, y)
        }
    }
}

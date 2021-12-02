package day2

class Position(val x: Int, val depth: Int) {

    fun move(vector: Vector): Position {
        when(vector.direction) {
            Direction.UP -> return Position(x, depth - vector.magnitude)
            Direction.DOWN -> return Position(x, depth + vector.magnitude)
            Direction.FORWARD -> return Position(x + vector.magnitude, depth)
        }
    }
}

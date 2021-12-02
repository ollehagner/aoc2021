package day2

class Position(val x: Int, val depth: Int) {

    fun move(direction: Direction, magnitude: Int): Position {
        when(direction) {
            Direction.UP -> return Position(x, depth - magnitude)
            Direction.DOWN -> return Position(x, depth + magnitude)
            Direction.FORWARD -> return Position(x + magnitude, depth)
        }
    }
}

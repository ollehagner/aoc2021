package day2

class Position(val x: Int, val depth: Int) {

    fun move(direction: SubmarineDirection, magnitude: Int): Position {
        when(direction) {
            SubmarineDirection.UP -> return Position(x, depth - magnitude)
            SubmarineDirection.DOWN -> return Position(x, depth + magnitude)
            SubmarineDirection.FORWARD -> return Position(x + magnitude, depth)
        }
    }
}

package day2

class Position(val x: Int, val depth: Int) {

    fun execute(command: Command): Position {
        when(command.direction) {
            Direction.UP -> return Position(x, depth - command.magnitude)
            Direction.DOWN -> return Position(x, depth + command.magnitude)
            Direction.FORWARD -> return Position(x + command.magnitude, depth)
        }
    }
}

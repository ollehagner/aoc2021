package day2

class SubmarineStatus(val position: Position, private val aim: Int) {

    fun executeCommand(command: Command): SubmarineStatus {
        when(command.direction) {
            Direction.UP -> return SubmarineStatus(position, aim - command.magnitude)
            Direction.DOWN -> return SubmarineStatus(position, aim + command.magnitude)
            Direction.FORWARD -> return SubmarineStatus(position.move(Direction.FORWARD, command.magnitude).move(Direction.DOWN, aim * command.magnitude), aim)
        }
    }

    override fun toString(): String {
        return "Status(position=x=${position.x} depth=${position.depth}, aim=$aim)"
    }

}

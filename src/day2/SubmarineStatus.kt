package day2



class SubmarineStatus(val position: Position, private val aim: Int) {

    fun executeCommand(command: Command): SubmarineStatus {
        when(command.direction) {
            SubmarineDirection.UP -> return SubmarineStatus(position, aim - command.magnitude)
            SubmarineDirection.DOWN -> return SubmarineStatus(position, aim + command.magnitude)
            SubmarineDirection.FORWARD -> return SubmarineStatus(position.move(SubmarineDirection.FORWARD, command.magnitude).move(SubmarineDirection.DOWN, aim * command.magnitude), aim)
        }
    }

    override fun toString(): String {
        return "Status(position=x=${position.x} depth=${position.depth}, aim=$aim)"
    }

}

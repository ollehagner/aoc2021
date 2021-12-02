package day2

class Command {

    constructor(stringValue: String) {
        val splitted = stringValue.split(" ")
        direction = Direction.valueOf(splitted.first().uppercase())
        magnitude = splitted.last().toInt()
    }

    val direction: Direction
    val magnitude: Int
    override fun toString(): String {
        return "Command(direction=$direction, magnitude=$magnitude)"
    }


}

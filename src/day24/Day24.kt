package day24

import java.lang.IllegalArgumentException

fun main() {

}

class ALU(val instructions: List<String>, val inputValues: Iterator<Long>) {

    val data = mutableMapOf("w" to 0L, "x" to 0L, "y" to 0L, "z" to 0L)

    fun execute(command: String) {
        val commandParts = command.split(" ")
        val commandName = commandParts[0]
        when(commandName) {
            "inp" -> write(command.substringAfter(" "), inputValues.next())
            "add" -> {
                val a = commandParts[1]
                val b = commandParts[2]
                write(a, read(a) + read(b))
            }
            "mul" -> {
                val a = commandParts[1]
                val b = commandParts[2]
                write(a, read(a) * read(b))
            }
            "div" -> {
                val a = commandParts[1]
                val b = commandParts[2]
                write(a, read(a) / read(b))
            }
        }
    }

    fun read(variableOrValue: String): Long {
        return data.getOrDefault(variableOrValue, variableOrValue.toLong())
    }

    private fun write(variable: String, value: Long) {
        if(!data.containsKey(variable)) {
            throw IllegalArgumentException("Key $variable doesn't exist")
        }
        data[variable] = value
    }
}
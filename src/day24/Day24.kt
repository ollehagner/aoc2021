package day24

import readInput

fun main() {

    fun calculateZ(params: Map<String, Long>, previousZ: Long, digit: Long): Long {
        if((previousZ % 26) + params["b"]!! != digit) {
            return (((previousZ / params["a"]!!) * 26) + params["c"]!! + digit)
        } else {
            return previousZ / params["a"]!!
        }
    }

    fun part1(instructions: List<String>) {
        val start = System.currentTimeMillis()
        val segments = instructions.chunked(18)

        var accumulatedZvalues = mutableMapOf(0L to 0L)
        segments.forEach { instructions ->
            val tempZValues = mutableMapOf<Long, Long>()
            val a = instructions[4].substringAfter("div z ").toLong()
            val b = instructions[5].substringAfter("add x ").toLong()
            val c = instructions[15].substringAfter("add y ").toLong()
            val params = mapOf("a" to a, "b" to b, "c" to c)
            accumulatedZvalues.forEach { z, modelNumber ->
                (1L..9L).forEach { digit ->
                    val newZValue = calculateZ(params, z, digit)
                    if(a == 1L || (a == 26L && newZValue < z)) {
                        tempZValues[newZValue] = maxOf(tempZValues[newZValue] ?: Long.MIN_VALUE, (modelNumber * 10) + digit)
                    }
                }
            }
            accumulatedZvalues = tempZValues
        }

        println("Finished in ${System.currentTimeMillis() - start} ms")
        println("Day 24 part 1. Largest valid model number: ${accumulatedZvalues.get(0)}")
    }

    fun part2(instructions: List<String>) {
        val start = System.currentTimeMillis()
        val segments = instructions.chunked(18)

        var accumulatedZvalues = mutableMapOf(0L to 0L)
        segments.forEach { instructions ->
            val tempZValues = mutableMapOf<Long, Long>()
            val a = instructions[4].substringAfter("div z ").toLong()
            val b = instructions[5].substringAfter("add x ").toLong()
            val c = instructions[15].substringAfter("add y ").toLong()
            val params = mapOf("a" to a, "b" to b, "c" to c)
            accumulatedZvalues.forEach { z, modelNumber ->
                (1L..9L).forEach { digit ->
                    val newZValue = calculateZ(params, z, digit)
                    if(a == 1L || (a == 26L && newZValue < z)) {
                        tempZValues[newZValue] = minOf(tempZValues[newZValue] ?: Long.MAX_VALUE, (modelNumber * 10) + digit)
                    }
                }
            }
            accumulatedZvalues = tempZValues
        }

        println("Finished in ${System.currentTimeMillis() - start} ms")
        println("Day 24 part 1. Largest valid model number: ${accumulatedZvalues.get(0)}")
    }

    val instructions = readInput("day24/day24")

    part1(instructions)
    part2(instructions)

}

class ALU(state: Map<String, Long> = mapOf("w" to 0L, "x" to 0L, "y" to 0L, "z" to 0L)) {

    val data = HashMap(state)

    fun execute(instructions: List<String>, input: List<Long>): ALU {
        val inputValues = input.iterator()
        instructions.forEach { runInstruction(it, inputValues) }
        return this
    }

    private fun runInstruction(instruction: String, inputValues: Iterator<Long>) {
        val commandParts = instruction.split(" ")
        val commandName = commandParts[0]
        when (commandName) {
            "inp" -> write(instruction.substringAfter(" "), inputValues.next())
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
            "mod" -> {
                val a = commandParts[1]
                val b = commandParts[2]
                write(a, read(a) % read(b))
            }
            "eql" -> {
                val a = commandParts[1]
                val b = commandParts[2]
                write(a, if (read(a) == read(b)) 1 else 0)
            }
        }
        println(instruction + " : " + data)
    }

    private fun read(variableOrValue: String): Long {
//        println(data.getOrDefault("x", 100))
        return data.getOrElse(variableOrValue) { variableOrValue.toLong() }
    }

    private fun write(variable: String, value: Long) {
        if (!data.containsKey(variable)) {
            throw IllegalArgumentException("Key $variable doesn't exist")
        }
        data[variable] = value
    }

    override fun toString(): String {
        return "ALU(data=$data)"
    }


}

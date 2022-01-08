package day24

fun main() {

}

class ALU(val instructions: List<String>) {

    val data = mapOf("w" to 0L, "x" to 0L, "y" to 0L, "z" to 0L)

    fun read(variableOrValue: String): Long {
        return data.getOrDefault(variableOrValue, variableOrValue.toLong())
    }

    fun write()
}
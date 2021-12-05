package common

data class Point(val x: Int, val y: Int) {
    constructor(values: String) :
        this(values.split(",").first().toInt(), values.split(",").last().toInt())
}

package day18

import readInput
import java.math.BigInteger
import java.util.*

fun main() {

    fun findExplodeIndex(number: String): Optional<Int> {
        return Optional.ofNullable(number
            .mapIndexed { index, character -> Pair(index, character) }
            .filter { (_, character) -> character == '[' || character == ']' }
            .scan(Pair(0, 0)) { acc, bracketPair ->
                if (bracketPair.second == '[') Pair(bracketPair.first, acc.second + 1) else Pair(bracketPair.first, acc.second - 1)
            }
            .firstOrNull { (index, openBracketCount) -> openBracketCount >= 5 }?.first)
    }

    val explode: (String, Int) -> String = { number: String, pairToExplodeIndex: Int ->
        val exploded = StringBuilder()

        val leftExplodeValue = number.substring(pairToExplodeIndex + 1).substringBefore(",")
        val rightExplodeValue = number.substring(pairToExplodeIndex + 1).substringAfter((",")).substringBefore("]")
        val afterPairToExplodeIndex = pairToExplodeIndex + leftExplodeValue.length + rightExplodeValue.length + 3

        val digitsToTheLeft = number.substring(0, pairToExplodeIndex - 1)
            .mapIndexed { index, character -> NumberPart(index, character) }.reversed()
            .dropWhile { !it.value.isDigit() }.takeWhile { it.value.isDigit() }.toList().reversed()
        if (digitsToTheLeft.isNotEmpty()) {
            exploded.append(number.substring(0, digitsToTheLeft.first().index))
            val newValue = digitsToTheLeft.map{ it.value }.joinToString("").toInt() + leftExplodeValue.toInt()
            exploded.append(newValue)
            exploded.append(number.substring(digitsToTheLeft.last().index + 1, pairToExplodeIndex))
        } else {
            exploded.append(number.substring(0, pairToExplodeIndex))
        }
        exploded.append("0")

        val digitsToTheRight = number.substring(afterPairToExplodeIndex).mapIndexed { index, character -> NumberPart(index, character) }
            .dropWhile { !it.value.isDigit() }.takeWhile { it.value.isDigit() }
        if (digitsToTheRight.isNotEmpty()) {
            val newValue = rightExplodeValue.toInt() + digitsToTheRight.map{ it.value }.joinToString("").toInt()
            exploded.append(number.substring(afterPairToExplodeIndex, afterPairToExplodeIndex + digitsToTheRight.first().index))
            exploded.append(newValue)
            exploded.append(number.substring(afterPairToExplodeIndex + digitsToTheRight.last().index + 1))
        } else {
            exploded.append(number.substring(afterPairToExplodeIndex))
        }
        exploded.toString()
    }

    fun findSplitIndex(number: String): Optional<Int> {
        val match = "\\d{2}".toRegex().find(number)
        return Optional.ofNullable(match?.value)
            .map { number.indexOf(it)!! }
    }

    val split: (String, Int) -> String =  { number, index ->
        val valueToSplit = BigInteger(number.substring(index, index + 2))
        val valueAndRemainder = valueToSplit.divideAndRemainder(BigInteger("2"))
        val leftValue = valueAndRemainder.first().toInt()
        val rightValue = valueAndRemainder.sumOf { it.toInt() }
        number.substring(0, index) + "[$leftValue,$rightValue]" + number.substring(index + 2)
    }

    fun hasNextAction(number: String): Boolean {
        return sequenceOf(findExplodeIndex(number), findSplitIndex(number))
            .any { it.isPresent }
    }

    fun reduce(number: String): String {
        var currentNumber = number
        while(hasNextAction(currentNumber)) {
            val explodeIndex = findExplodeIndex(currentNumber)
            if(explodeIndex.isPresent) {
                currentNumber = explode(currentNumber, explodeIndex.get())
            } else {
                val splitIndex = findSplitIndex(currentNumber)
                currentNumber = split(currentNumber, splitIndex.get())
            }
        }
        return currentNumber
    }

    fun add(left: String, right: String): String {
        return reduce("[$left,$right]")
    }

    fun magnitude(number: String): Long {
        if(number.all { it.isDigit() }) {
            return number.toLong()
        } else {
            return magnitude("\\[(\\d+),(\\d+)\\]".toRegex().replace(number) { matchResult ->
                val left = matchResult.groupValues[1]
                val right = matchResult.groupValues[2]
                "${left.toLong() * 3 + right.toLong() * 2}"
            })
        }
    }

    fun part1(numbers: List<String>) {
        val sum = numbers
            .drop(1)
            .fold(numbers.first()) { acc, next -> add(acc, next) }
        println("Day 18 part 1. Total magnitude : ${magnitude(sum)}")
    }

    fun part2(numbers: List<String>) {

    }

    val testinput = readInput("day18/day18_test_3")
    val input = readInput("day18/day18")

//    part1(input)
    part2(testinput)

}

data class NumberPart(val index: Int, val value: Char) {

    fun digitValue(): Int {
        return value.digitToInt()
    }

}

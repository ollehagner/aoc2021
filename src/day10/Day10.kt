package day10

import readInput
import java.util.*

fun main() {
    val chunkTypes = listOf(Pair('{', '}'), Pair('(', ')'), Pair('[', ']'), Pair('<', '>'))
    val openingTypes = chunkTypes.map { it.first }

    fun closingTypeFor(openingType: Char): Char {
        return chunkTypes.first { openingType == it.first }.second
    }

    fun openingTypeFor(closingType: Char): Char {
        return chunkTypes.first { closingType == it.second }.first
    }

    fun validateChunk(chunk: CharArray): ChunkValidationResult {
        val stack = LinkedList<Char>()
        val invalidCharacter = chunk
            .firstOrNull { character ->
                var invalidState = false
                if(character in openingTypes) {
                    stack.push(character)
                } else {
                    invalidState = stack.pop() != openingTypeFor(character)
                }
                invalidState
            }

        if(invalidCharacter != null) {
            return ChunkValidationResult(ChunkValidationStatus.INVALID_CHARACTER, stack, invalidCharacter)
        } else if(stack.isNotEmpty()) {
            return ChunkValidationResult(ChunkValidationStatus.INCOMPLETE, stack, invalidCharacter)
        } else {
            return ChunkValidationResult(ChunkValidationStatus.VALID, stack, invalidCharacter)
        }
    }

    fun part1(chunks: List<CharArray>) {
        val totalSyntaxErrorScore = chunks
            .map { validateChunk(it) }
            .filter { it.status == ChunkValidationStatus.INVALID_CHARACTER }
            .map { chunkValidationResult ->
                when(chunkValidationResult.invalidCharacter) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> throw IllegalArgumentException("Unknown closing character ${chunkValidationResult.invalidCharacter}")
                }
            }
            .sum()
        println("Day 10 part 1. Total syntax error score: $totalSyntaxErrorScore")
    }

    fun part2(chunks: List<CharArray>) {
        val autoCompleteScores = chunks
            .map { validateChunk(it) }
            .filter { it.status == ChunkValidationStatus.INCOMPLETE }
            .map { it.stack }
            .map { stack ->
                stack
                    .map { closingTypeFor(it) }
                    .map {
                        when(it) {
                            ')' -> 1
                            ']' -> 2
                            '}' -> 3
                            '>' -> 4
                            else -> throw IllegalArgumentException("Unknown closing character ${closingTypeFor(it)}")
                        }
                    }
                    .fold(0L) { acc, value -> (acc * 5) + value }
            }
            .sorted()
        val middleIndex = (autoCompleteScores.size - 1) / 2
        println("Day 10 part 2. Total autocomplete score: ${autoCompleteScores[middleIndex]}")

    }

    val testInput = readInput("day10/day10_test")
    val input = readInput("day10/day10")

    part1(input.map { it.toCharArray() })
    part2(input.map { it.toCharArray() })



}

data class ChunkValidationResult(val status: ChunkValidationStatus, val stack: Deque<Char>, val invalidCharacter: Char?) {

}

enum class ChunkValidationStatus {
    VALID, INCOMPLETE, INVALID_CHARACTER


}

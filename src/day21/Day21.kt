package day21

val DIRAC_DICE_VALUES =  mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)

fun main() {

    fun deterministicDie(): Sequence<Int> {
        return sequence {
            var value = 0
            while (true) {
                value++
                yield(value)
                if (value >= 100) {
                    value = 0
                }
            }
        }
    }


    fun part1(player1StartPosition: Int, player2StartPosition: Int) {

        val boards = deterministicDie().mapIndexed { index, roll ->
            DiceRoll(index + 1, roll)
        }
            .chunked(3)
            .map { DiceRoll(it.last().rollCount, it.sumOf { roll -> roll.value }) }
            .chunked(2)
            .scan(Pair(Board(player1StartPosition), Board(player2StartPosition))) { acc, rolls ->
                val player1Score = acc.first.move(rolls.first())
                if (player1Score < 1000) {
                    val player2Score = acc.second.move(rolls.last())
                }
                acc
            }
            .takeWhile { it.first.score < 1000 && it.second.score < 1000 }
            .last()

        val winner = listOf(boards.first, boards.second).maxByOrNull { it.score }!!
        val loser = listOf(boards.first, boards.second).minByOrNull { it.score }!!
        println("Day 21 part 1. Result: ${winner.latestDiceRollCount * loser.score}")

    }


    fun iterate(boards: List<MultiverseBoard>): List<MultiverseBoard> {
        val boardMap = mutableMapOf<Int, MultiverseBoard>()
        boards.forEach { board ->
            DIRAC_DICE_VALUES.forEach { (roll, universes) ->
                val copy = board.copy(
                        player1 = board.player1.copy(),
                        player2 = board.player2.copy(),
                        universes = board.universes * universes
                    )
                copy.move(roll)
                val key = "${copy.player1.position}${copy.player1.score}${copy.player2.position}${copy.player2.score}".toInt()
                boardMap.merge(key, copy) { boardToAdd, accBoard ->
                    accBoard.copy(accBoard.player1.copy(), accBoard.player2.copy(), universes = accBoard.universes + boardToAdd.universes)
                }
            }
        }
        return boardMap.values.toList()
    }

    fun part2(player1StartPosition: Int, player2StartPosition: Int) {

        val counter = mutableMapOf<Int, Long>(1 to 0, 2 to 0)

        var boardList = listOf(MultiverseBoard(Player(player1StartPosition), Player(player2StartPosition),
            universes = 1
        ))

        while(boardList.isNotEmpty()) {
            boardList.filter { it.hasWinner() }
                .forEach { counter[it.winner()] = counter[it.winner()]!! + it.universes }
            boardList = iterate(boardList.filter { !it.hasWinner() }.toList())
        }
        val max = counter.maxOf { it.value }
        println("Day 21 part 2. The winner wins in $max universes")

    }


//    part1(8, 4)
    part2(8, 4)

}

data class DiceRoll(val rollCount: Int, val value: Int) {}

data class Board(var position: Int) {

    var score = 0
    var latestDiceRollCount = 0

    fun move(roll: DiceRoll): Int {
        position = (position + roll.value) % 10
        position = if (position != 0) position else 10
        score += position
        latestDiceRollCount = roll.rollCount

        return score
    }

}

data class MultiverseBoard(
    val player1: Player, val player2: Player, var currentPlayerIndex: Int = 0, val universes: Long) {

    val players = listOf(player1, player2)

    fun move(roll: Int) {
        var currentPlayer = players[currentPlayerIndex]
        currentPlayer.position = calculatePosition(currentPlayer.position, roll)
        currentPlayer.score += currentPlayer.position
        currentPlayerIndex = if (currentPlayerIndex == 1) 0 else 1
    }

    private fun calculatePosition(currentPosition: Int, steps: Int): Int {
        var newPosition = (currentPosition + steps) % 10
        return if (newPosition == 0) 10 else newPosition
    }

    fun hasWinner(): Boolean {
        return players.any { it.score >= 21 }
    }

    fun winner(): Int {
        return (players.indexOfFirst { it.score >= 21 } + 1)
    }

    fun leaderScore(): Int {
        return players.maxOf { it.score }
    }

}

data class Player(var position: Int, var score: Int = 0)

package day04

import Utils
import asInt

private data class Field(val value: Int, var marked: Boolean = false)

private class Board(init: List<String>) {
    private var size: Int
    private val rows: Array<Array<Field>>
    private var markedRowCount: Array<Int>
    private var markedColumnCount: Array<Int>

    init {
        val multiWhiteSpaceRegex = Regex(""" +""")
        rows = init.map { it.trim().split(multiWhiteSpaceRegex).asInt() }
            .map { line -> line.map { Field(it) }.toTypedArray() }.toTypedArray()
        size = rows.size
        markedColumnCount = Array(size) { 0 }
        markedRowCount = Array(size) { 0 }
    }

    fun mark(value: Int): Boolean {
        rows.forEachIndexed { rowIndex, row ->
            row.withIndex().find { (_, field) -> !field.marked && field.value == value }?.let { (columnIndex, field) ->
                field.marked = true
                markedColumnCount[columnIndex]++
                markedRowCount[rowIndex]++
            }
        }
        return markedRowCount.any { it >= size } || markedColumnCount.any { it >= size }
    }

    fun sumUnchecked(): Int {
        return rows.sumOf { row -> row.sumOf { if (!it.marked) it.value else 0 } }
    }
}

fun main() {
    val utils = Utils(4)

    fun createGame(input: String): Pair<List<Int>, List<Board>> {
        val sections = input.split("\n\n")
        val numbers = sections.first().split(",").asInt()

        val boards = sections.drop(1).map { Board(it.split("\n")) }

        return numbers to boards
    }

    fun part1(input: String): Int {
        val (numbers, boards) = createGame(input)
        var winner: Board? = null
        val numbersIterator = numbers.iterator()
        var num: Int? = null
        while (winner == null && numbersIterator.hasNext()) {
            num = numbersIterator.next()
            for (b in boards) {
                if (b.mark(num)) {
                    winner = b
                    break;
                }
            }
        }
        if (num == null) error("No numbers drawn")
        if (winner == null) error("No winner")

        return winner.sumUnchecked() * num
    }

    fun part2(input: String): Int {
        val (numbers, boards) = createGame(input)
        var lastWinner: Board? = null
        var winnerNumber: Int? = null

        numbers.fold(boards) { acc, num ->
            acc.filter { board ->
                val won = board.mark(num)
                if (won) {
                    lastWinner = board
                    winnerNumber = num
                }

                return@filter !won
            }
        }

        if (lastWinner == null) error("No winner")

        return lastWinner!!.sumUnchecked() * winnerNumber!!
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readFile("test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    // Solve puzzle and print result
    val input = utils.readFile()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
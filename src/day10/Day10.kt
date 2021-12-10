package day10

import Utils
import java.util.*

fun main() {
    val utils = Utils(10)

    val openToClose = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    /**
     * Checks the syntax and returns the first invalid character (or null if valid)
     * @param stack pass an empty Stack-object to retrieve the eventually missing closing chars
     **/
    fun String.checkSyntax(stack: Stack<Char>? = null): Char? {
        val syntaxStack = stack ?: Stack<Char>()
        for (c in this) {
            if (c in openToClose.keys) {
                syntaxStack.push(openToClose[c])
            } else {
                if (syntaxStack.pop() != c) {
                    return c
                }
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        val scoreMap = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137,
        )

        return input.fold(0) { score, line ->
            score + (line.checkSyntax()?.let { scoreMap[it]!! } ?: 0)
        }
    }

    fun part2(input: List<String>): Long {
        val scoreMap = mapOf(
            ')' to 1,
            ']' to 2,
            '}' to 3,
            '>' to 4,
        )

        val scores = input.mapNotNull { line ->
            val remainingStack = Stack<Char>()

            if (line.checkSyntax(remainingStack) == null) {
                var score = 0L

                // Can't use fold here as this iterates from first to top element (reversed().fold() would work)
                while (!remainingStack.empty()) {
                    score = score * 5 + scoreMap[remainingStack.pop()]!!
                }

                return@mapNotNull score
            }

            return@mapNotNull null
        }.sorted()

        return scores[scores.size / 2]
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
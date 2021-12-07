package day07

import Utils
import asInt
import kotlin.math.abs

fun main() {
    val utils = Utils(7)

    fun List<Int>.calculateMinCosts(costFunction: (start: Int, end: Int) -> Int): Int {
        val costs = (minOrNull()!!..maxOrNull()!!).map { alignPosition ->
            sumOf { costFunction(it, alignPosition) }
        }
        return costs.minOrNull()!!
    }

    fun part1(input: List<Int>): Int {
        return input.calculateMinCosts { start, end -> abs(end - start) }
    }

    fun part2(input: List<Int>): Int {
        return input.calculateMinCosts { start, end ->
            val n = abs(end - start)
            n * (n + 1) / 2
        }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readFile("test").split(',').asInt()
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    // Solve puzzle and print result
    val input = utils.readFile().split(',').asInt()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
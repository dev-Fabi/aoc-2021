package day01

import Utils
import asInt

fun main() {
    val utils = Utils(1)

    fun part1(input: List<String>): Int {
        return input.asInt()
            .zipWithNext()
            .count { it.second > it.first }
    }

    fun part2(input: List<String>): Int {
        return input.asInt()
            .windowed(3)
            .map { it.sum() }
            .zipWithNext()
            .count { it.second > it.first }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = utils.readLines("test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}

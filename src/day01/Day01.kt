package day01

import Utils

fun main() {
    val utils = Utils(1)

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = utils.readLines("test")
    check(part1(testInput) == 1)
    check(part2(testInput) == 1)

    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}

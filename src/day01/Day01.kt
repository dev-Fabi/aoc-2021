package day01

import Utils
import asInt

fun main() {
    val utils = Utils(1)

    /**
     * Counts how many times the following number in the list is bigger than the previous one
     */
    fun List<Int>.countConsecutiveIncreases() = zipWithNext().count { (prev, next) -> next > prev }

    // Count how many times the consecutive number increases
    fun part1(input: List<Int>): Int {
        return input.countConsecutiveIncreases()
    }

    // Count how many times the sum of a sliding window of 3 numbers increases
    fun part2(input: List<Int>): Int {
        return input.windowed(3) { it.sum() }.countConsecutiveIncreases()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test").asInt()
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    // Solve puzzle and print result
    val input = utils.readLines().asInt()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}

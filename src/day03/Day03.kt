package day03

import Utils

fun main() {
    val utils = Utils(3)

    fun <T> Pair<List<T>, List<T>>.takeLarger() = if (first.size >= second.size) first else second
    fun <T> Pair<List<T>, List<T>>.takeSmaller() = if (first.size < second.size) first else second

    fun List<String>.filterBitwise(mostCommon: Boolean) = first().indices.fold(this) { acc, columnIndex ->
        if (acc.size == 1) return acc.single() // Quick exit

        val partitioned = acc.partition { it[columnIndex] == '1' }
        if (mostCommon) partitioned.takeLarger() else partitioned.takeSmaller()
    }.single()

    fun part1(input: List<String>): Int {
        val half = input.size / 2
        val gamma = input.first().indices.map { columnIndex ->
            if (input.count { it[columnIndex] == '1' } <= half) '0' else '1'
        }.joinToString("")
        val epsilon = gamma.map { if (it == '1') '0' else '1' }.joinToString("")

        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun part2(input: List<String>): Int {
        return input.filterBitwise(true).toInt(2) * input.filterBitwise(false).toInt(2)
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
package day08

import Utils
import kotlin.math.pow

fun main() {
    val utils = Utils(8)

    // List of signals to digits ([acedgfb, cdfbe, gcdfa, fbcad, dab, cefabd, cdfgeb, eafb, cagedb, ab] -> [cdfeb, fcadb, cdfeb, cdbaf])
    fun List<String>.getEntries() = map { entry ->
        val (signalPattern, digitOutput) = entry.split(" | ")
        val inputs = signalPattern.split(" ").map { it.toSet() }
        val outputs = digitOutput.split(" ").map { it.toSet() }
        inputs to outputs
    }

    fun List<Int>.toNumber() = reversed().withIndex().sumOf { (index, digit) ->
        digit * (10.0).pow(index.toDouble())
    }.toInt()

    // Count of active segments to corresponding number
    val uniqueSegmentCounts = mapOf(
        2 to 1,
        4 to 4,
        3 to 7,
        7 to 8,
    )

    fun part1(input: List<String>): Int {
        val entries = input.getEntries()
        return entries.sumOf { signal -> signal.second.count { it.size in uniqueSegmentCounts.keys } }
    }

    fun part2(input: List<String>): Int {
        val entries = input.getEntries()

        val numbers = entries.map { entry ->
            // Map obvious patterns (unique length)
            val signalsMap = entry.first.associateWith { uniqueSegmentCounts[it.size] }.toMutableMap()

            val one = signalsMap.entries.first { it.value == 1 }.key
            val fourWithoutOne = signalsMap.entries.first { it.value == 4 }.key.minus(one)

            // Map the rest of the patterns (not unique length)
            signalsMap.filter { it.value == null }.forEach {
                val digit = when (it.key.size) {
                    5 -> when { // 3, 5, 2
                        it.key.intersect(one).size == 2 -> 3
                        it.key.intersect(fourWithoutOne).size == 1 -> 2
                        else -> 5
                    }
                    6 -> when { // 0, 9, 6
                        it.key.intersect(one).size == 1 -> 6
                        it.key.intersect(fourWithoutOne).size == 1 -> 0
                        else -> 9
                    }
                    else -> null
                }

                signalsMap[it.key] = digit
            }

            return@map entry.second.mapNotNull { signalsMap[it] }.toNumber()
        }

        return numbers.sum()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
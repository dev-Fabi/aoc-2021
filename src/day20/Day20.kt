package day20

import Utils

fun main() {
    val utils = Utils(20)

    fun List<String>.countLightPixels() = sumOf { it.sumOf(Char::digitToInt) }

    fun enhance(input: List<String>, times: Int): List<String> {
        fun Char.toImageBit() = when (this) {
            '.' -> 0
            '#' -> 1
            else -> error("Check input")
        }

        fun List<String>.getEnhanceIndex(pixelLine: Int, pixelRow: Int, default: Char): Int {
            return (-1..1).joinToString("") { lineOffset ->
                val line = this.getOrNull(pixelLine + lineOffset)
                (-1..1).joinToString("") { (line?.getOrNull(pixelRow + it) ?: default).toString() }
            }.toInt(2)
        }

        fun List<String>.enhance(alg: String, defaultValue: Char): List<String> {
            return (-1..this.count()).map { lineIndex ->
                (-1..this.first().count()).joinToString("") { rowIndex ->
                    alg[this.getEnhanceIndex(lineIndex, rowIndex, defaultValue)].toString()
                }
            }
        }

        val alg = input.first().map(Char::toImageBit).joinToString("")
        val startImage = input.drop(2).map { it.map(Char::toImageBit).joinToString("") }

        return (1..times).fold(startImage to '0') { acc, _ ->
            acc.first.enhance(alg, acc.second) to if (acc.second == '0') alg.first() else alg.last()
        }.first
    }

    fun part1(input: List<String>): Int {
        return enhance(input, 2).countLightPixels()
    }

    fun part2(input: List<String>): Int {
        return enhance(input, 50).countLightPixels()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}

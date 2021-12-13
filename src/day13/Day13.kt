package day13

import Utils

private data class Fold(val axis: Char, val value: Int)

fun main() {
    val utils = Utils(13)

    fun init(input: String): Pair<List<MutableList<Boolean>>, List<Fold>> {
        val (pointStrings, instructionStrings) = input.split("\n\n").map { it.split("\n") }

        val points = pointStrings.map { line ->
            val (x, y) = line.split(",")
            return@map x.toInt() to y.toInt()
        }

        val instructions = instructionStrings.map { line ->
            val (axis, value) = line.removePrefix("fold along ").split("=")
            return@map Fold(axis.single(), value.toInt())
        }

        val maxX = points.maxOf { it.first }
        val maxY = points.maxOf { it.second }

        val transparent = List(maxY + 1) { MutableList(maxX + 1) { false } }
        points.forEach { (x, y) ->
            transparent[y][x] = true
        }

        return Pair(transparent, instructions)
    }

    fun List<List<Boolean>>.foldY(value: Int): List<List<Boolean>> {
        val newList = List(value) { MutableList(this.first().size) { false } }
        for (i in 1..value) {
            for (x in 0 until this.first().size) {
                newList[value - i][x] = this[value - i][x] || (this.getOrNull(value + i)?.get(x) == true)
            }
        }

        return newList
    }

    fun List<List<Boolean>>.foldX(value: Int): List<List<Boolean>> {
        val newList = List(this.size) { MutableList(value) { false } }
        for (i in 1..value) {
            for (y in 0 until this.size) {
                newList[y][value - i] = this[y][value - i] || this[y][value + i]
            }
        }

        return newList
    }

    fun part1(transparent: List<List<Boolean>>, instructions: List<Fold>): Int {
        val firstInstruction = instructions.first()
        val result = if (firstInstruction.axis == 'x') {
            transparent.foldX(firstInstruction.value)
        } else {
            transparent.foldY(firstInstruction.value)
        }

        return result.sumOf { row -> row.count { it } }
    }

    fun part2(transparent: List<List<Boolean>>, instructions: List<Fold>): List<String> {
        val result = instructions.fold(transparent) { folded, instruction ->
            if (instruction.axis == 'x') {
                folded.foldX(instruction.value)
            } else {
                folded.foldY(instruction.value)
            }
        }

        return result.map { row ->
            row.joinToString("") { if (it) "#" else "." }
        }
    }

    // Test if implementation meets criteria from the description
    val (testTransparent, testInstructions) = init(utils.readFile("test"))
    check(part1(testTransparent, testInstructions) == 17)
    val part2Expected = """
        #####
        #...#
        #...#
        #...#
        #####
        .....
        .....
    """.trimIndent()
    check(part2(testTransparent, testInstructions).joinToString("\n") == part2Expected)

    // Solve puzzle and print result
    val (transparent, instructions) = init(utils.readFile())
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(transparent, instructions))
    println("\tPart 2:\n" + part2(transparent, instructions).joinToString("\n\t\t", prefix = "\t\t"))
}
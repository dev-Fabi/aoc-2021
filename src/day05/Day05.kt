package day05

import Utils
import increase
import toward

private data class Coordinate(val x: Int, val y: Int) {
    companion object {
        fun getIdFor(x: Int, y: Int) = "$x,$y"
    }
}

private data class Line(val start: Coordinate, val end: Coordinate)

private fun String.toCoordinates() = split(",").let { (x, y) -> Coordinate(x.toInt(), y.toInt()) }

private fun List<Line>.calculateOverlapping(): Int {
    val map = mutableMapOf<String, Int>()
    forEach { line ->
        if (line.start.x == line.end.x) {
            // vertical line -> not representable as a function
            for (y in line.start.y toward line.end.y) {
                map.increase(Coordinate.getIdFor(line.start.x, y))
            }
        } else {
            val k = (line.start.y - line.end.y) / (line.start.x - line.end.x)
            val d = -(k * line.start.x - line.start.y)

            for (x in line.start.x toward line.end.x) {
                map.increase(Coordinate.getIdFor(x, k * x + d))
            }
        }
    }
    return map.count { it.value >= 2 }
}

fun main() {
    val utils = Utils(5)

    fun List<String>.toLines() = map { line ->
        line.split(" -> ").let { (start, end) -> Line(start.toCoordinates(), end.toCoordinates()) }
    }

    fun part1(lines: List<Line>): Int {
        return lines.filter { it.start.x == it.end.x || it.start.y == it.end.y }
            .calculateOverlapping()
    }

    fun part2(lines: List<Line>): Int {
        return lines.calculateOverlapping()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test").toLines()
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    // Solve puzzle and print result
    val input = utils.readLines().toLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
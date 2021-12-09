package day09

import Utils

fun main() {
    val utils = Utils(9)

    fun List<String>.toHeightMap() = map { row -> row.map { it.digitToInt() } }
    fun Pair<Int, Int>.getAdjacents(maxX: Int, maxY: Int): List<Pair<Int, Int>> {
        val (x, y) = this
        val adjacents = mutableListOf<Pair<Int, Int>>()

        if (x - 1 >= 0) adjacents.add(x - 1 to y)
        if (x + 1 <= maxX) adjacents.add(x + 1 to y)
        if (y - 1 >= 0) adjacents.add(x to y - 1)
        if (y + 1 <= maxY) adjacents.add(x to y + 1)

        return adjacents
    }

    fun List<List<Int>>.getAdjacents(x: Int, y: Int) =
        (x to y).getAdjacents(this.size - 1, this.first().size - 1)


    fun List<List<Int>>.getLowestPoints(): List<Pair<Int, Int>> {
        val lowPoints = mutableListOf<Pair<Int, Int>>()
        for (x in this.indices) {
            for (y in this.first().indices) {
                val current = this[x][y]

                if (this.getAdjacents(x, y).all { current < this[it.first][it.second] }) {
                    lowPoints.add(x to y)
                }
            }
        }
        return lowPoints
    }

    fun part1(heightMap: List<List<Int>>): Int {
        val lowPoints = heightMap.getLowestPoints()
        return lowPoints.sumOf { (x, y) -> heightMap[x][y] + 1 }
    }

    fun part2(heightMap: List<List<Int>>): Int {
        val maxX = heightMap.size - 1
        val maxY = heightMap.first().size - 1
        val visited = Array(heightMap.size) { Array(heightMap.first().size) { false } }

        fun getBasinSize(point: Pair<Int, Int>): Int {
            val (x, y) = point
            val value = heightMap[x][y]

            val higherAdjacents = point.getAdjacents(maxX, maxY)
                // Get Adjacents that are higher (flow to this point) and were not arleady counted (visited)
                .filter { (aX, aY) -> !visited[aX][aY] && heightMap[aX][aY].let { it != 9 && it > value } }
                .onEach { (aX, aY) -> visited[aX][aY] = true }

            return higherAdjacents.fold(higherAdjacents.size) { count, adj ->
                count + getBasinSize(adj)
            }
        }

        val lowPoints = heightMap.getLowestPoints()
        val basins = lowPoints.map {
            visited[it.first][it.second] = true
            getBasinSize(it) + 1 // Add low point itself
        }

        return basins.sorted().takeLast(3).reduce { acc, a -> acc * a }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test").toHeightMap()
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    // Solve puzzle and print result
    val input = utils.readLines().toHeightMap()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
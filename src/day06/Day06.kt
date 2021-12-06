package day06

import Utils

fun main() {
    val utils = Utils(6)

    fun simulatePopulation(initialAgeList: List<Int>, days: Int): Long {
        val fishAgeMap = initialAgeList.groupBy { it }
            .map { it.key to it.value.count().toLong() }
            .toMap().toMutableMap()

        for (i in 0 until days) {
            fishAgeMap.keys.sorted().forEach {
                if (it == -1) return@forEach
                fishAgeMap[it - 1] = fishAgeMap[it] ?: 0
                fishAgeMap[it] = 0
            }
            fishAgeMap[8] = fishAgeMap[-1] ?: 0
            fishAgeMap[6] = (fishAgeMap[6] ?: 0) + (fishAgeMap[-1] ?: 0)
            fishAgeMap[-1] = 0
        }

        return fishAgeMap.values.sum()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readFile("test").split(",").map { it.toInt() }
    check(simulatePopulation(testInput, 18) == 26L)
    check(simulatePopulation(testInput, 80) == 5934L)
    check(simulatePopulation(testInput, 256) == 26984457539)

    // Solve puzzle and print result
    val input = utils.readFile().split(",").map { it.toInt() }
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + simulatePopulation(input, 80))
    println("\tPart 2: " + simulatePopulation(input, 256))
}
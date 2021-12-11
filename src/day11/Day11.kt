package day11

import Utils
import forEach2D
import kotlin.math.max
import kotlin.math.min

private class DumboOctopus(initialEnergyLevel: Int) {
    private var flashed = false
    private val adjacents = mutableSetOf<DumboOctopus>()
    var flashCount = 0
        private set
    var energyLevel: Int
        private set

    init {
        this.energyLevel = initialEnergyLevel
    }

    fun increase() {
        if (flashed) return // Octopus has already flashed this step

        energyLevel++
        if (energyLevel > 9) {
            flashed = true
            flashCount++
            adjacents.forEach { it.increase() }
        }
    }

    fun addAdjacent(adj: DumboOctopus) {
        if (adj == this) return
        adjacents.add(adj)
    }

    fun endStep() {
        if (flashed) {
            energyLevel = 0
        }
        flashed = false
    }
}

fun main() {
    val utils = Utils(11)

    fun init(input: List<String>): List<List<DumboOctopus>> {
        val map = input.map { line -> line.map { DumboOctopus(it.digitToInt()) } }
        map.forEach2D { (x, y), octopus ->
            for (aX in max(x - 1, 0)..min(x + 1, map.size - 1)) {
                for (aY in max(y - 1, 0)..min(y + 1, map.first().size - 1)) {
                    octopus.addAdjacent(map[aX][aY])
                }
            }
        }

        return map
    }

    fun simulateStep(map: List<List<DumboOctopus>>) {
        map.forEach2D { _, octopus ->
            octopus.increase()
        }
        map.forEach2D { _, octopus ->
            octopus.endStep()
        }
    }

    fun part1(input: List<String>): Int {
        val map = init(input)

        for (i in 1 until 100) {
            simulateStep(map)
        }

        return map.fold(0) { count, row ->
            row.fold(count) { c, o -> c + o.flashCount }
        }
    }

    fun part2(input: List<String>): Int {
        val map = init(input)

        var stepCount = 0
        while (map.any { row -> row.any { it.energyLevel != 0 } }) {
            stepCount++
            simulateStep(map)
        }

        return stepCount
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
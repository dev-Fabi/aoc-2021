package day12

import Utils

private sealed class Cave(val id: String) {
    private val adjacents = mutableSetOf<Cave>()

    fun addAdjacent(adj: Cave) {
        if (adj == this) return
        adjacents.add(adj)
    }

    fun getAdjacents(): Set<Cave> {
        return adjacents
    }
}

private class SmallCave(id: String) : Cave(id)
private class BigCave(id: String) : Cave(id)
private class StartCave(id: String) : Cave(id)
private class EndCave(id: String) : Cave(id)

private class CaveSystem() {
    private val caveMap = mutableMapOf<String, Cave>()
    val startCave
        get() = caveMap["start"]!!

    fun getCaveForId(id: String): Cave {
        if (caveMap.containsKey(id)) {
            return caveMap[id]!!
        }

        val newCave = when {
            id == "start" -> StartCave(id)
            id == "end" -> EndCave(id)
            id.all { it.isUpperCase() } -> BigCave(id)
            else -> SmallCave(id)
        }
        caveMap[id] = newCave
        return newCave
    }

    companion object {
        fun fromInput(input: List<String>): CaveSystem {
            val caveSystem = CaveSystem()

            input.forEach { path ->
                val (from, to) = path.split("-")
                val fromCave = caveSystem.getCaveForId(from)
                val toCave = caveSystem.getCaveForId(to)

                fromCave.addAdjacent(toCave)
                toCave.addAdjacent(fromCave)
            }

            return caveSystem
        }
    }
}

fun main() {
    val utils = Utils(12)

    fun buildPaths(
        currentCave: Cave,
        visited: Set<SmallCave>,
        currentPath: List<Cave>,
        canVisitSmallCaveTwice: Boolean = false
    ): List<List<Cave>> {
        val foundPaths = mutableListOf<List<Cave>>()

        currentCave.getAdjacents()
            .filter { (canVisitSmallCaveTwice || it !in visited) && it !is StartCave }
            .forEach { nextCave ->
                if (nextCave is EndCave) {
                    foundPaths.add(currentPath.plus(nextCave))
                } else {
                    foundPaths.addAll(
                        buildPaths(
                            currentCave = nextCave,
                            visited = if (nextCave is SmallCave) visited.plus(nextCave) else visited,
                            currentPath = currentPath.plus(nextCave),
                            canVisitSmallCaveTwice = if (canVisitSmallCaveTwice && nextCave in visited) false else canVisitSmallCaveTwice
                        )
                    )
                }
            }

        return foundPaths
    }

    fun part1(caveSystem: CaveSystem): Int {
        val foundPaths = buildPaths(caveSystem.startCave, emptySet(), listOf(caveSystem.startCave))

        //foundPaths.forEach { println(it.joinToString(" -> ") { it.id }) }

        return foundPaths.size
    }

    fun part2(caveSystem: CaveSystem): Int {
        val foundPaths = buildPaths(caveSystem.startCave, emptySet(), listOf(caveSystem.startCave), true)

        //foundPaths.forEach { println(it.joinToString(" -> ") { it.id }) }

        return foundPaths.size
    }

    // Test if implementation meets criteria from the description
    val testInputs = arrayOf(
        CaveSystem.fromInput(utils.readLines("test")),
        CaveSystem.fromInput(utils.readLines("test2")),
        CaveSystem.fromInput(utils.readLines("test3"))
    )
    // Check part 1
    check(part1(testInputs[0]) == 10)
    check(part1(testInputs[1]) == 19)
    check(part1(testInputs[2]) == 226)
    // Check part 2
    check(part2(testInputs[0]) == 36)
    check(part2(testInputs[1]) == 103)
    check(part2(testInputs[2]) == 3509)

    // Solve puzzle and print result
    val input = CaveSystem.fromInput(utils.readLines())
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
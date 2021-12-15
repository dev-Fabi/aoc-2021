package day14

import Utils
import increase

fun main() {
    val utils = Utils(14)

    fun simulatePolymer(input: List<String>, n: Int): Long {
        val templatePolymer = input.first();

        val rules = input.drop(2).associate { rule ->
            val (from, insert) = rule.split(" -> ")

            from to listOf(
                from.first() + insert,
                insert + from.last()
            )
        }

        val initialPolymers = templatePolymer.zipWithNext { a, b -> "$a$b" }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        val countMap = (1..n).fold(initialPolymers) { map, _ ->
            buildMap {
                map.forEach { (key, value) ->
                    rules[key]!!.forEach { set(it, getOrDefault(it, 0).plus(value)) }
                }
            }
        }

        val elementCount = buildMap<Char, Long> {
            countMap.forEach { (key, value) ->
                increase(key.first(), value)
            }
            increase(templatePolymer.last()) // Add the last element which is only part of one pair as the second Element
        }

        return elementCount.maxOf { it.value } - elementCount.minOf { it.value }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(simulatePolymer(testInput, 10) == 1588L)
    check(simulatePolymer(testInput, 40) == 2188189693529)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + simulatePolymer(input, 10))
    println("\tPart 2: " + simulatePolymer(input, 40))
}
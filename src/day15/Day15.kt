package day15

import Utils
import java.util.*

fun main() {
    val utils = Utils(15)

    fun List<String>.toRiskMap() = map { row -> row.map { it.digitToInt() } }
    fun List<List<Int>>.toGraph() = withIndex().map { (x, row) ->
        row.withIndex().map { (y, c) -> Node(x, y, c) }
    }

    fun List<Int>.incrementWrappedBy(n: Int) = map {
        val incremented = it + n
        if (incremented <= 9) incremented else 1 + (incremented % 10)
    }

    fun part1(input: List<String>): Long {
        return input.toRiskMap().toGraph().getMinRisk()
    }

    fun part2(input: List<String>): Long {
        val replicatedRight = input.toRiskMap().map { row ->
            (0..4).map { n ->
                row.incrementWrappedBy(n)
            }.flatten()
        }
        val fullMap = (0..4).flatMap { n ->
            replicatedRight.map { it.incrementWrappedBy(n) }
        }

        return fullMap.toGraph().getMinRisk()
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test")
    check(part1(testInput) == 40L)
    check(part2(testInput) == 315L)

    // Solve puzzle and print result
    val input = utils.readLines()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}

data class Node(val x: Int, val y: Int, val weight: Int, var distance: Long = Long.MAX_VALUE)

// Uses Dijkstra-Algorithm but without keeping track of the path (only the cost/distance)
private fun List<List<Node>>.getMinRisk(): Long {
    fun Node.getNeighbours() = listOfNotNull(
        this@getMinRisk.getOrNull(this.x - 1)?.getOrNull(this.y),
        this@getMinRisk.getOrNull(this.x + 1)?.getOrNull(this.y),
        this@getMinRisk.getOrNull(this.x)?.getOrNull(this.y - 1),
        this@getMinRisk.getOrNull(this.x)?.getOrNull(this.y + 1)
    )

    val queue = PriorityQueue(compareBy(Node::distance))

    // init
    val startNode = this[0][0]
    startNode.distance = 0
    queue.add(startNode)

    // Get current node
    var node: Node? = queue.poll()
    while (node != null) {
        node.getNeighbours().forEach {
            val newCost = node!!.distance + it.weight
            when {
                queue.contains(it) && newCost < node!!.distance -> it.distance = newCost

                it.distance == Long.MAX_VALUE -> {
                    it.distance = newCost
                    queue.add(it)
                }
            }
        }
        node = queue.poll()
    }

    return this.last().last().distance
}
package day02

import Utils

private sealed class Command(val amount: Int) {
    class Forward(amount: Int) : Command(amount);
    class Down(amount: Int) : Command(amount);
    class Up(amount: Int) : Command(amount);
}

fun main() {
    val utils = Utils(2)

    fun List<String>.toCommands(): List<Command> {
        val cmdRegex = Regex("""(\w*?) (\d+)""")
        return map { line ->
            val (cmd, amount) = cmdRegex.find(line)!!.destructured
            when (cmd) {
                "forward" -> Command.Forward(amount.toInt())
                "down" -> Command.Down(amount.toInt())
                "up" -> Command.Up(amount.toInt())
                else -> error("$cmd not supported")
            }
        }
    }

    fun part1(input: List<Command>): Int {
        return input.fold(0 to 0) { (x, y), cmd ->
            when (cmd) {
                is Command.Forward -> x + cmd.amount to y
                is Command.Down -> x to y + cmd.amount
                is Command.Up -> x to y - cmd.amount
            }
        }.let { (x, y) -> x * y }
    }

    fun part2(input: List<Command>): Int {
        return input.fold(Triple(0, 0, 0)) { (x, y, aim), cmd ->
            when (cmd) {
                is Command.Forward -> Triple(x + cmd.amount, y + aim * cmd.amount, aim)
                is Command.Down -> Triple(x, y, aim + cmd.amount)
                is Command.Up -> Triple(x, y, aim - cmd.amount)
            }
        }.let { (x, y, _) -> x * y }
    }

    // Test if implementation meets criteria from the description
    val testInput = utils.readLines("test").toCommands()
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    // Solve puzzle and print result
    val input = utils.readLines().toCommands()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}
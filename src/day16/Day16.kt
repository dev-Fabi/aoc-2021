package day16

import Utils

fun main() {
    val utils = Utils(16)

    fun String.toBinaryString() = map {
        when (it) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> error("Invalid hex char $it")
        }
    }.joinToString("")

    fun part1(input: String): Int {
        val binaryString = input.toBinaryString()

        val pkg = Package.parse(binaryString)

        return pkg.versionSum()
    }

    fun part2(input: String): Long {
        val binaryString = input.toBinaryString()

        val pkg = Package.parse(binaryString)

        return pkg.value
    }

    // Test if implementation meets criteria from the description
    var pkg: Package = Package.parse("D2FE28".toBinaryString())
    pkg as LiteralValue
    check(pkg.value == 2021L)

    pkg = Package.parse("38006F45291200".toBinaryString()) as Operator
    check(pkg.subPackages.count() == 2)
    check((pkg.subPackages[0] as LiteralValue).value == 10L)
    check((pkg.subPackages[1] as LiteralValue).value == 20L)

    pkg = Package.parse("EE00D40C823060".toBinaryString()) as Operator
    check(pkg.subPackages.count() == 3)
    check((pkg.subPackages[0] as LiteralValue).value == 1L)
    check((pkg.subPackages[1] as LiteralValue).value == 2L)
    check((pkg.subPackages[2] as LiteralValue).value == 3L)

    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)

    // Solve puzzle and print result
    val input = utils.readFile()
    println("Solution day ${utils.day}:")
    println("\tPart 1: " + part1(input))
    println("\tPart 2: " + part2(input))
}

sealed class Package {
    abstract val version: Int
    abstract val type: Int
    abstract val length: Int
    abstract val value: Long

    fun versionSum(): Int {
        return when (this) {
            is LiteralValue -> this.version
            is Operator -> this.version + this.subPackages.sumOf { it.versionSum() }
        }
    }

    companion object {
        fun parse(binaryString: String): Package {
            val version = binaryString.take(3).toInt(2)
            val type = binaryString.drop(3).take(3).toInt(2)
            val binaryContent = binaryString.drop(6)

            return if (type == 4) {
                LiteralValue.parse(version, binaryContent)
            } else {
                Operator.parse(version, type, binaryContent)
            }
        }
    }
}

class LiteralValue(override val version: Int, override val length: Int, override val value: Long) : Package() {
    override val type: Int = 4

    companion object {
        fun parse(version: Int, binaryContent: String): LiteralValue {
            val groups = binaryContent.chunked(5)
            val bits = StringBuilder("")
            var processedGroups = 0
            for (g in groups) {
                bits.append(g.drop(1))
                processedGroups++
                if (g.startsWith("0")) {
                    break
                }
            }

            return LiteralValue(version, bits.length + processedGroups + 6, bits.toString().toLong(2))
        }
    }
}

sealed class Operator(
    override val version: Int,
    override val type: Int,
    val subPackages: List<Package>,
    headerLength: Int,
) : Package() {
    override val length: Int = headerLength + subPackages.sumOf { it.length }

    companion object {
        fun parse(version: Int, type: Int, binaryContent: String): Operator {
            var toProcess = binaryContent.drop(1)
            val subPackages = mutableListOf<Package>()

            return if (binaryContent.startsWith("0")) {
                val numberOfBits = toProcess.take(15).toInt(2)
                toProcess = toProcess.drop(15)
                var parsedBits = 0

                while (parsedBits < numberOfBits) {
                    val pkg = Package.parse(toProcess)
                    toProcess = toProcess.drop(pkg.length)
                    parsedBits += pkg.length
                    subPackages.add(pkg)
                }

                Operator.createFor(version, type, subPackages, 6 + 16)
            } else {
                val numberOfSubPackages = toProcess.take(11).toInt(2)
                toProcess = toProcess.drop(11)

                var parsedPackages = 0

                while (parsedPackages < numberOfSubPackages) {
                    val pkg = Package.parse(toProcess)
                    toProcess = toProcess.drop(pkg.length)
                    parsedPackages++
                    subPackages.add(pkg)
                }

                Operator.createFor(version, type, subPackages, 6 + 12)
            }
        }

        private fun createFor(version: Int, type: Int, subPackages: List<Package>, headerLength: Int): Operator {
            return when (type) {
                Sum.type -> Sum(version, subPackages, headerLength)
                Product.type -> Product(version, subPackages, headerLength)
                Min.type -> Min(version, subPackages, headerLength)
                Max.type -> Max(version, subPackages, headerLength)
                GreaterThan.type -> GreaterThan(version, subPackages, headerLength)
                LessThan.type -> LessThan(version, subPackages, headerLength)
                EqualTo.type -> EqualTo(version, subPackages, headerLength)
                else -> error("Unexpected operation type: $type")
            }
        }
    }
}

class Sum(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {

    override val value: Long = this.subPackages.sumOf { it.value }

    companion object {
        const val type = 0
    }
}

class Product(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {

    override val value: Long

    init {
        var product = 1L
        for (element in this.subPackages) {
            product *= element.value
        }
        value = product
    }

    companion object {
        const val type = 1
    }
}

class Min(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {
    override val value: Long = this.subPackages.minOf { it.value }

    companion object {
        const val type = 2
    }
}

class Max(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {
    override val value: Long = this.subPackages.maxOf { it.value }

    companion object {
        const val type = 3
    }
}

class GreaterThan(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {
    override val value: Long = if (this.subPackages[0].value > this.subPackages[1].value) 1 else 0

    init {
        require(this.subPackages.count() == 2)
    }

    companion object {
        const val type = 5
    }
}

class LessThan(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {
    override val value: Long = if (this.subPackages[0].value < this.subPackages[1].value) 1 else 0

    init {
        require(this.subPackages.count() == 2)
    }

    companion object {
        const val type = 6
    }
}

class EqualTo(version: Int, subPackages: List<Package>, headerLength: Int) :
    Operator(version, type, subPackages, headerLength) {
    override val value: Long = if (this.subPackages[0].value == this.subPackages[1].value) 1 else 0

    init {
        require(this.subPackages.count() == 2)
    }

    companion object {
        const val type = 7
    }
}


import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class Utils(val day: Int) {
    private val dayString: String = day.toString().padStart(2, '0')

    /**
     * Reads lines from the given input txt file.
     */
    fun readLines(suffix: String? = null) = getFile(suffix).readLines()

    /**
     * Reads the complete text from given input file.
     */
    fun readFile(suffix: String? = null) = getFile(suffix).readText()

    private fun getFile(suffix: String?): File {
        val fileName = StringBuilder("Day${dayString}")
        suffix?.let { fileName.append("_$suffix") }
        fileName.append(".txt")

        return File("src/day$dayString", fileName.toString())
    }
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Converts string list to int list
 */
fun List<String>.asInt(): List<Int> = map { it.toInt() }
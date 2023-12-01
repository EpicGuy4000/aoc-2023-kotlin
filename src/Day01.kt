fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val first = it.first { c -> c.isDigit() }.digitToInt()
            val last = it.last { c -> c.isDigit() }.digitToInt()
            first * 10 + last
        }
    }

    fun part2(input: List<String>): Int {
        val stringsAndValues = mutableMapOf(
            Pair("one", "1"),
            Pair("two", "2"),
            Pair("three", "3"),
            Pair("four", "4"),
            Pair("five", "5"),
            Pair("six", "6"),
            Pair("seven", "7"),
            Pair("eight", "8"),
            Pair("nine", "9")
        )

        return input.sumOf {
            var newIt = it
            stringsAndValues.onEach { str -> newIt = newIt.replace(str.key, str.key + str.value + str.key) }

            val first = newIt.first { c -> c.isDigit() }.digitToInt()
            val last = newIt.last { c -> c.isDigit() }.digitToInt()

            first * 10 + last
        }
    }

    // test if implementation meets criteria from the description, like:
    checkExpectedValue(part2(listOf("two1nine")), 29)
    checkExpectedValue(part2(listOf("eightwothree")), 83)

    val testInput = readInput("Day01_test")
    checkExpectedValue(part1(testInput), 142)
    val testInput2 = readInput("Day01_test2")
    checkExpectedValue(part2(testInput2), 281)


    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

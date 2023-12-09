fun predictNext(history: List<Int>): Long {
    var current = history.toList()
    val lastValues = buildList {
        add(current.last())
        while (!current.all { x -> x == 0 }) {
            current = current.zipWithNext { x, y ->
                y - x
            }
            add(current.last())
        }
    }

    return lastValues.sum().toLong()
}

fun main() {
    fun part1(input: List<String>): Long = input.map { it.split(" ").map { x -> x.toInt() } }
            .sumOf(::predictNext)

    fun part2(input: List<String>): Long  = input.map { it.split(" ").reversed().map { x -> x.toInt() } }
            .sumOf(::predictNext)

    // test if implementation meets criteria from the description, like:

    checkExpectedValue(18, part1(listOf("0 3 6 9 12 15")))

    val testInput = readInput("Day09_test")
    checkExpectedValue(114, part1(testInput))
    checkExpectedValue(2, part2(testInput))

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

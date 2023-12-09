fun main() {
    fun part1(input: List<String>): Long =
            input.map { it.split(" ").filterNot { x -> x.isBlank() }.drop(1).map { x -> x.toInt() } }
                    .zipWithNext { a, b -> a.zip(b) }.flatten()
                    .fold(1) { acc, (time, distance) ->
                        acc * (1..<time).count { i -> i * (time - i) > distance }
                    }

    fun part2(input: List<String>): Long =
            input.map { it.split(" ").filterNot { x -> x.isBlank() }.drop(1).joinToString("").toLong() }
                    .zipWithNext()
                    .fold(1) { acc, (time, distance) ->
                        acc * (1..<time).count { i -> i * (time - i) > distance }
                    }

    // test if implementation meets criteria from the description, like:

    val testInput = readInput("Day06_test")
    checkExpectedValue(288, part1(testInput))
    checkExpectedValue(71503, part2(testInput))

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

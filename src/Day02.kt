fun main() {
    fun parseGame(it: String): Pair<Int, List<Map<String, Int>>> {
        val gameRegex = Regex("""Game (\d+): (.+)""")
        val matches = gameRegex.find(it)!!.groups
        val id = matches[1]!!.value.toInt()
        val draws = matches[2]!!.value.split("; ")
                .map { draw ->
                    draw.split(", ").associate { cubeGroup ->
                        val (count, color) = cubeGroup.split(" ")
                        Pair(color, count.toInt())
                    }
                }
        return Pair(id, draws)
    }

    fun part1(input: List<String>): Int {
        val availableCubes = mapOf(Pair("red", 12), Pair("blue", 14), Pair("green", 13))

        val sum = input.sumOf {
            val (id, draws) = parseGame(it)

            val isGamePossible = draws.all { set -> set.all { cubeGroup -> cubeGroup.value <= availableCubes[cubeGroup.key]!! } }
            if (isGamePossible) id else 0
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val sum = input.sumOf {
            val minCubeCounts = mapOf(Pair("red", 0), Pair("blue", 0), Pair("green", 0))

            val (_, draws) = parseGame(it)

            val power = minCubeCounts.entries.fold(1) { mul, pair ->
                mul * draws.maxOf { draw ->
                    draw[pair.key] ?: 0
                }
            }

            power
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    checkExpectedValue(8, part1(testInput))
    checkExpectedValue(2286, part2(testInput))

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

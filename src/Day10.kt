data class Coordinates(val x: Int, val y: Int)

val globalValidMoves = mapOf(
    Pair('S', listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1)) ),
    Pair('|', listOf(Pair(0, 1), Pair(0, -1))),
    Pair('L', listOf(Pair(0, -1), Pair(1, 0))),
    Pair('J', listOf(Pair(0, -1), Pair(-1, 0))),
    Pair('7', listOf(Pair(-1, 0), Pair(0, 1))),
    Pair('F', listOf(Pair(1, 0), Pair(0, 1))),
    Pair('-', listOf(Pair(1, 0), Pair(-1, 0))),
    Pair('.', listOf())
)

class Cursor(private val start: Coordinates, val map: List<String>) {
    private var current = start
    private var invalidMove = Pair(0, 0)
    var movesMade = 0
    private val pipesInLoop = mutableSetOf(start)

    fun isInLoop(coordinates: Coordinates): Boolean = pipesInLoop.contains(coordinates)

    private fun moveOnce() {
        val move = globalValidMoves[map[current.y][current.x]]!!
            .filterNot { it == invalidMove }
            .first {
                globalValidMoves[map[current.y + it.second][current.x + it.first]]!!.contains(
                    Pair(
                        -it.first,
                        -it.second
                    )
                )
            }

        current = Coordinates(current.x + move.first, current.y + move.second)
        pipesInLoop.add(current)

        invalidMove = Pair(-move.first, -move.second)
    }

    fun moveFullLap() {
        do {
            moveOnce()
            movesMade++
        } while ( current != start)
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val row = input.withIndex()
            .find {
                it.value.contains('S')
            }!!

        val cursor = Cursor(Coordinates(row.value.indexOf('S'), row.index), input)
        cursor.moveFullLap()

        return (cursor.movesMade / 2).toLong()
    }

    fun part2(input: List<String>): Long {
        val row = input.withIndex()
            .find {
                it.value.contains('S')
            }!!

        val cursor = Cursor(Coordinates(row.value.indexOf('S'), row.index), input)
        cursor.moveFullLap()

        return input.withIndex().sumOf {
            var inLoop = false
            val y = it.index

            val counter = it.value.withIndex().fold(0) { acc, x ->
                val coordinates = Coordinates(x.index, y)
                val isCoordinatePartOfLoop = cursor.isInLoop(coordinates)

                when {
                    !isCoordinatePartOfLoop && !inLoop -> {
                        acc
                    }
                    !isCoordinatePartOfLoop && inLoop -> {
                        acc + 1
                    }
                    else -> {
                        inLoop = if (input[y][x.index] == '-') inLoop else !inLoop
                        println("$coordinates was in loop (${input[coordinates.y][coordinates.x]}). Counter state: $acc. Loop state: $inLoop")
                        acc
                    }
                }
            }.toLong()

            println("in row $it counted $counter")
            counter
        }
    }

    // test if implementation meets criteria from the description, like:
    val simpleLoopInput = """.....
.S-7.
.|.|.
.L-J.
.....""".split("\r\n", "\n")

    val simpleLoopWithExtraPipesInput = """-L|F7
7S-7|
L|7||
-L-J|
L|-JF""".split("\r\n", "\n")

    checkExpectedValue(4, part1(simpleLoopInput))
    checkExpectedValue(4, part1(simpleLoopWithExtraPipesInput))

    checkExpectedValue(8, part1(readInput("Day10_test")))
    //checkExpectedValue(4, part2(readInput("Day10_test2")))
    checkExpectedValue(10, part2(readInput("Day10_test3")))

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

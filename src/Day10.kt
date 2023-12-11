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

fun canMove(to: Char, direction: Pair<Int, Int>): Boolean =
        globalValidMoves[to]!!.contains(Pair(-direction.first, -direction.second))

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

        val inLoopX = mutableSetOf<Coordinates>()

        for (y in 0..input.lastIndex) {
            var enteringPipeChar = '.'
            var counter = 0

            fun currentlyInLoop() = counter % 2 == 1

            for (x in 0..row.value.lastIndex) {
                val coordinates = Coordinates(x, y)
                val isCoordinatePartOfLoop = cursor.isInLoop(coordinates)

                when {
                    !isCoordinatePartOfLoop && !currentlyInLoop() -> {
                        enteringPipeChar = '.'
                    }
                    !isCoordinatePartOfLoop && currentlyInLoop() -> {
                        inLoopX.add(coordinates)
                        enteringPipeChar = '.'
                    }
                    else -> {
                        if (enteringPipeChar == '.' || !canMove(input[y][x], Pair(1, 0))) {
                            enteringPipeChar = input[y][x]
                            counter++
                        }

                        if (enteringPipeChar == 'L' && input[y][x] == 'J'
                                || enteringPipeChar == 'F' && input[y][x] == '7') {
                            counter++
                        }
                    }
                }
            }
        }

        return inLoopX.size.toLong()
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
    checkExpectedValue(1, part2(readInput("Day10_test")))

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

class Node(val key: String) {
    var left: String? = null
    var right: String? = null

    operator fun get(lOrR: Char): String = when (lOrR) {
        'L' -> left!!
        'R' -> right!!
        else -> throw IndexOutOfBoundsException()
    }

    override fun toString(): String = "$key = (${left}, ${right})"
    override fun hashCode(): Int = key.hashCode()
    override fun equals(other: Any?): Boolean = other is Node && other.key == key
}

fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b // % is remainder
        a = temp
    }
    return a
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b));
}

fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1..input.lastIndex) {
        result = lcm(result, input[i])
    }

    return result
}


fun main() {
    fun part1(input: List<String>): Long {
        val movements = input.first()
        val nodeRegex = """^(\w+)\s+=\s+\((\w+),\s+(\w+)\)$""".toRegex()

        val parsed = input.drop(2).fold(mutableMapOf<String, Node>()) { nodes, line ->
            val (_, key, left, right) = nodeRegex.find(line)!!.groupValues

            val node = nodes[key] ?: Node(key)
            node.left = left
            node.right = right

            nodes[key] = node

            nodes
        }

        var current = parsed["AAA"]!!
        val target = parsed["ZZZ"]!!
        var steps = 0L

        while (current != target) {
            val i = (steps % movements.length).toInt()

            current = parsed[current[movements[i]]]!!
            steps++
        }

        return steps
    }

    fun part2(input: List<String>): Long {
        val movements = input.first()
        val nodeRegex = """^(\w+)\s+=\s+\((\w+),\s+(\w+)\)$""".toRegex()

        val parsed = input.drop(2).fold(mutableMapOf<String, Node>()) { nodes, line ->
            val (_, key, left, right) = nodeRegex.find(line)!!.groupValues

            val node = nodes[key] ?: Node(key)
            node.left = left
            node.right = right

            nodes[key] = node

            nodes
        }

        var currentNodes = parsed.values.filter { x -> x.key.endsWith('A') }
        var steps = 0L

        val x = currentNodes.map {
            var current = it
            var stepsLocal = 0
            while (!current.key.endsWith('Z')) {
                val i = (stepsLocal % movements.length)

                current = parsed[current[movements[i]]]!!
                stepsLocal++
            }

            stepsLocal
        }

        println(x)

        return lcm(x.map { it.toLong() })
    }

    // test if implementation meets criteria from the description, like:

    val testInput = readInput("Day08_test")
    checkExpectedValue(6, part1(listOf("LLR", "", "AAA = (BBB, BBB)", "BBB = (AAA, ZZZ)", "ZZZ = (ZZZ, ZZZ)")))
    checkExpectedValue(2, part1(testInput))
    checkExpectedValue(6, part2(readInput("Day08_test2")))

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

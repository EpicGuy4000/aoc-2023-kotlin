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

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(numbers: List<Long>): Long = numbers.fold(1) { lcm, n ->
    lcm * n / gcd(lcm, n)
}

fun main() {
    fun parseInput(input: List<String>): Pair<String, MutableMap<String, Node>> {
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
        return Pair(movements, parsed)
    }

    fun countStepsToTarget(isTarget: (Node) -> Boolean, startingNode: Node, movements: String, nodes: Map<String, Node>): Long {
        var current = startingNode
        var steps = 0

        while (!isTarget(current)) {
            val i = steps % movements.length

            current = nodes[current[movements[i]]]!!
            steps++
        }

        return steps.toLong()
    }

    fun part1(input: List<String>): Long {
        val (movements, nodes) = parseInput(input)

        return countStepsToTarget({c -> c == nodes["ZZZ"]!! }, nodes["AAA"]!!, movements, nodes)
    }

    fun part2(input: List<String>): Long {
        val (movements, nodes) = parseInput(input)

        return lcm(nodes.values.filter { x -> x.key.endsWith('A') }.map {
            countStepsToTarget({ c -> c.key.endsWith('Z') }, it, movements, nodes)
        })
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

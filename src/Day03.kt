fun main() {
    fun readNumber(row: String, startIndex: Int, also: (Int) -> Unit = { }): Int {
        var currentNumber = row[startIndex].digitToInt()
        var nextChar = row[startIndex + 1]

        var i = startIndex
        while (nextChar.isDigit()) {
            currentNumber = currentNumber * 10 + nextChar.digitToInt()

            i++
            nextChar = row.getOrElse(i + 1) { '.' }
            also(i + 1)
        }

        return currentNumber
    }

    fun part1(input: List<String>): Int {
        val emptyRow = ".".repeat(input[0].length)

        return input.withIndex().sumOf {
            val rowAbove = input.getOrElse(it.index - 1) { emptyRow }
            val rowBelow = input.getOrElse(it.index + 1) { emptyRow }
            val neighbors = mutableListOf<Char>()

            var rowSum = 0
            var i = 0
            while (i < it.value.length)
            {
                val char = it.value[i]
                if (!char.isDigit()) {
                    i++
                    continue
                }

                neighbors.add(it.value.getOrElse(i - 1) { '.' }) //left
                neighbors.add(rowAbove.getOrElse(i - 1) { '.' }) //left up diag
                neighbors.add(rowBelow.getOrElse(i - 1) { '.' }) //left down diag

                neighbors.add(rowAbove[i]) //up
                neighbors.add(rowBelow[i]) //down

                neighbors.add(it.value.getOrElse(i + 1) { '.' }) //right
                neighbors.add(rowAbove.getOrElse(i + 1) { '.' }) //right up diag
                neighbors.add(rowBelow.getOrElse(i + 1) { '.' }) //right down diag

                val currentNumber = readNumber(it.value, i) { index ->
                    neighbors.add(it.value.getOrElse(index) { '.' }) //right
                    neighbors.add(rowAbove.getOrElse(index) { '.' }) //right up diag
                    neighbors.add(rowBelow.getOrElse(index) { '.' }) //right down diag
                }

                rowSum += if (neighbors.any { n -> !n.isDigit() && n != '.' }) currentNumber else 0

                neighbors.clear()
                i += currentNumber.toString().length
            }

            rowSum
        }
    }

    fun part2(input: List<String>): Int {
        val emptyRow = ".".repeat(input[0].length)

        return input.withIndex().sumOf {
            val rowAbove = input.getOrElse(it.index - 1) { emptyRow }
            val rowBelow = input.getOrElse(it.index + 1) { emptyRow }
            val neighbors = mutableListOf<Triple<Char, Int, String>>()

            var rowSum = 0
            var i = 0
            while (i < it.value.length)
            {
                val char = it.value[i]
                if (char != '*') {
                    i++
                    continue
                }

                neighbors.add(Triple(it.value.getOrElse(i - 1) { '.' }, i - 1, it.value)) //left
                neighbors.add(Triple(rowAbove.getOrElse(i - 1) { '.' }, i - 1, rowAbove)) //left up diag
                neighbors.add(Triple(rowBelow.getOrElse(i - 1) { '.' }, i - 1, rowBelow)) //left down diag

                neighbors.add(Triple(rowAbove.getOrElse(i) { '.' }, i, rowAbove)) //up
                neighbors.add(Triple(rowBelow.getOrElse(i) { '.' }, i, rowBelow)) //down

                neighbors.add(Triple(it.value.getOrElse(i + 1) { '.' }, i + 1, it.value)) //right
                neighbors.add(Triple(rowAbove.getOrElse(i + 1) { '.' }, i + 1, rowAbove)) //right up diag
                neighbors.add(Triple(rowBelow.getOrElse(i + 1) { '.' }, i + 1, rowBelow)) //right down diag

                val neighbouringNumbers = neighbors.filter { n -> n.first.isDigit() }
                        .map { n ->
                            var firstDigit = n
                            while (firstDigit.third.getOrElse(firstDigit.second - 1) { '.' }.isDigit()) {
                                firstDigit = Triple(firstDigit.third[firstDigit.second - 1], firstDigit.second - 1, firstDigit.third)
                            }
                            firstDigit
                        }
                        .distinct()

                if (neighbouringNumbers.count() != 2) {
                    i++
                    println("neighbours are ${neighbouringNumbers.joinToString(",")}")
                    neighbors.clear()
                    continue
                }

                val gearMul = neighbouringNumbers
                    .fold(1) { acc, n -> acc * readNumber(n.third, n.second) }

                rowSum += gearMul

                neighbors.clear()
                i++
            }

            rowSum
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    checkExpectedValue(0, part1(listOf("467..114..")))
    checkExpectedValue(467, part1(listOf("467..114..", "...*......")))
    checkExpectedValue(4361, part1(testInput))
    checkExpectedValue(100, part2(listOf("10*10")))
    checkExpectedValue(100, part2(listOf("10.10", "..*..")))
    checkExpectedValue(100, part2(listOf("10.10", ".*...", "10...")))
    checkExpectedValue(467835, part2(testInput))

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

import kotlin.math.pow

data class Scratchcard(val cardNumber: Int, val winningNumbers: List<Int>, val yourNumbers: List<Int>, var cardCount: Int = 1){
    fun getWinningNumberCount() = yourNumbers.count { n -> winningNumbers.contains(n) }
}

val cardRegex = """Card\s+(\d+):\s+((?:\d+\s+)*)\|((?:\s+\d+)*)""".toRegex()

fun parseScratchcard(input: String): Scratchcard {
    val groups = cardRegex.find(input)!!.groups
    val cardNumber = groups[1]!!.value.toInt()
    val winningNumbers = groups[2]!!.value.split(" ").filterNot { s -> s.isBlank() }.map(String::toInt)
    val yourNumbers = groups[3]!!.value.split(" ").filterNot { s -> s.isBlank() }.map(String::toInt)
    return Scratchcard(cardNumber, winningNumbers, yourNumbers)
}

fun main() {
    fun part1(input: List<String>): Int =
        input.sumOf { 2.0.pow(parseScratchcard(it).getWinningNumberCount() - 1).toInt() }

    fun part2(input: List<String>): Int {
        val cards = input.map(::parseScratchcard).associateBy { it.cardNumber }

        cards.forEach { card ->
            (card.key + 1..card.key + card.value.getWinningNumberCount())
                .forEach { cards[it]!!.cardCount += card.value.cardCount }
        }

        return cards.values.sumOf { it.cardCount }
    }

    // test if implementation meets criteria from the description, like:

    checkExpectedValue(8, part1(listOf("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53")))
    checkExpectedValue(2, part1(listOf("Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19")))
    checkExpectedValue(1, part1(listOf("Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83")))
    checkExpectedValue(0, part1(listOf("Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36")))

    val testInput = readInput("Day04_test")
    checkExpectedValue(13, part1(testInput))
    checkExpectedValue(30, part2(testInput))

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

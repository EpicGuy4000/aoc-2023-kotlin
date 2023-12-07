enum class HandType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind,
}

data class Hand(val cards: List<Int>, val bid: Long, val type: HandType)

fun main() {
    fun part1(input: List<String>): Long {
        val labelMap = mapOf(
            Pair('2', 2),
            Pair('3', 3),
            Pair('4', 4),
            Pair('5', 5),
            Pair('6', 6),
            Pair('7', 7),
            Pair('8', 8),
            Pair('9', 9),
            Pair('T', 10),
            Pair('J', 12),
            Pair('Q', 13),
            Pair('K', 14),
            Pair('A', 15),
        )

        val handsInOrderOfStrength = input.map {
            val (handString, bidString) = it.split(" ")
            val bid = bidString.toLong()
            val hand = handString.map { c -> labelMap[c]!! }

            val handCounts = hand.fold(mutableMapOf<Int, Int>()) { counter, card ->
                counter[card] = (counter[card] ?: 0) + 1
                counter
            }.values.sortedByDescending { e -> e }

            val type = when {
                handCounts[0] == 5 -> HandType.FiveOfAKind
                handCounts[0] == 4 -> HandType.FourOfAKind
                handCounts[0] == 3 && handCounts[1] == 2 -> HandType.FullHouse
                handCounts[0] == 3 -> HandType.ThreeOfAKind
                handCounts[0] == 2 && handCounts[1] == 2 -> HandType.TwoPair
                handCounts[0] == 2 -> HandType.OnePair
                else -> HandType.HighCard
            }

            Hand(hand, bid, type)
        }.sortedWith(
            compareBy( { it.type }, { it.cards[0] }, { it.cards[1] }, { it.cards[2] }, { it.cards[3] }, { it.cards[4] } ))

        println(handsInOrderOfStrength.withIndex().joinToString("\n"))

        return handsInOrderOfStrength.withIndex()
            .sumOf { (it.index + 1) * it.value.bid }
    }

    fun part2(input: List<String>): Long {
        val labelMap = mapOf(
            Pair('J', 1),
            Pair('2', 2),
            Pair('3', 3),
            Pair('4', 4),
            Pair('5', 5),
            Pair('6', 6),
            Pair('7', 7),
            Pair('8', 8),
            Pair('9', 9),
            Pair('T', 10),
            //Pair('J', 12),
            Pair('Q', 13),
            Pair('K', 14),
            Pair('A', 15),
        )

        val handsInOrderOfStrength = input.map {
            val (handString, bidString) = it.split(" ")
            val bid = bidString.toLong()
            val hand = handString.map { c -> labelMap[c]!! }

            val handCounts = hand.fold(mutableMapOf(Pair(1, 0), Pair(2, 0), Pair(3, 0)) ) { counter, card ->
                counter[card] = (counter[card] ?: 0) + 1
                counter
            }

            val handCountsSorted = handCounts.entries.sortedByDescending { e -> e.value }

            var mostPresentCard = Pair(handCountsSorted[0].key, handCountsSorted[0].value)
            var secondMostPresentCard = Pair(handCountsSorted[1].key, handCountsSorted[1].value)
            var thirdMostPresentCard = Pair(handCountsSorted[2].key, handCountsSorted[2].value)

            val jCounts = handCounts[1] ?: 0
            if (jCounts in 1..4) {
                if (mostPresentCard.first != 1) {
                    mostPresentCard = Pair(mostPresentCard.first, mostPresentCard.second + jCounts)
                    if (secondMostPresentCard.first == 1) {
                        secondMostPresentCard = thirdMostPresentCard
                    }
                } else {
                    mostPresentCard = Pair(secondMostPresentCard.first, secondMostPresentCard.second + jCounts)
                    secondMostPresentCard = thirdMostPresentCard
                }
            }

            val type = when {
                mostPresentCard.second == 5 -> HandType.FiveOfAKind
                mostPresentCard.second == 4 -> HandType.FourOfAKind
                mostPresentCard.second == 3 && secondMostPresentCard.second == 2 -> HandType.FullHouse
                mostPresentCard.second == 3 -> HandType.ThreeOfAKind
                mostPresentCard.second == 2 && secondMostPresentCard.second == 2 -> HandType.TwoPair
                mostPresentCard.second == 2 -> HandType.OnePair
                else -> HandType.HighCard
            }

            Hand(hand, bid, type)
        }.sortedWith(
            compareBy( { it.type }, { it.cards[0] }, { it.cards[1] }, { it.cards[2] }, { it.cards[3] }, { it.cards[4] } ))

        println(handsInOrderOfStrength.withIndex().joinToString("\n"))

        return handsInOrderOfStrength.withIndex()
            .sumOf { (it.index + 1) * it.value.bid }
    }

    // test if implementation meets criteria from the description, like:

    val testInput = readInput("Day07_test")
    checkExpectedValue(6440, part1(testInput))
    checkExpectedValue(5905, part2(testInput))

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ")[1].split(" ")
            .map { it.toLong() }
            .associateBy { it }
            .toMutableMap()

        val processed = mutableSetOf<Long>()

        for (line in input.drop(3))
        {
            if (line.isBlank() || line.contains("map")) {
                processed.clear()
                continue
            }

            val (destinationStart, sourceStart, step) = line.split(" ").map(String::toLong)

            seeds.onEach {
                val delta = it.value - sourceStart
                seeds[it.key] = if (it.value in sourceStart..<(sourceStart + step) && !processed.contains(it.key)) {
                    processed.add(it.key)
                    destinationStart + delta
                } else it.value
            }
        }

        return seeds.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        var seedRanges = input.first().split(": ")[1].split(" ")
            .asSequence()
            .map { it.toLong() }
            .zipWithNext()
            .withIndex()
            .filter { it.index % 2 == 0 }
            .sortedBy { it.value.first }
            .map { it.value.first..<it.value.first + it.value.second }
            .toMutableSet()

        val newSeedRanges = mutableSetOf<LongRange>()
        var unprocessedRanges = mutableSetOf<LongRange>()
        unprocessedRanges.addAll(seedRanges)

        println("At start seed ranges are $seedRanges")

        for (line in input.drop(3))
        {
            if (line.isBlank() || line.contains("map")) {
                if (newSeedRanges.any()) {
                    seedRanges = newSeedRanges.asIterable().union(unprocessedRanges).sortedBy { it.first }.toMutableSet()
                    newSeedRanges.clear()
                    unprocessedRanges.clear()
                    unprocessedRanges.addAll(seedRanges)
                }

                if (line.isNotBlank()) {
                    println("Before $line seed ranges are $seedRanges")
                }

                continue
            }

            val (destinationStart, sourceStart, step) = line.split(" ").map(String::toLong)

            val sourceRange = (sourceStart..<sourceStart + step)
            val delta = destinationStart - sourceStart

            val newUnprocessedRanges = mutableSetOf<LongRange>()

            unprocessedRanges
                .onEach {
                    var rangeToProcess = LongRange.EMPTY

                    when {
                        sourceRange.first > it.last || sourceRange.last < it.first -> {
                            newUnprocessedRanges.add(it)
                        }
                        sourceRange.first <= it.first && sourceRange.last >= it.last -> {
                            rangeToProcess = it
                        }
                        sourceRange.first < it.first -> {
                            newUnprocessedRanges.add(min(sourceStart + step, it.last)..it.last)

                            rangeToProcess = it.first..sourceRange.last
                        }
                        sourceRange.last > it.last -> {
                            newUnprocessedRanges.add(it.first..sourceRange.first)

                            rangeToProcess = sourceRange.first ..it.last
                        }
                        else -> {
                            newUnprocessedRanges.add(it.first..<sourceRange.first)
                            newUnprocessedRanges.add(sourceRange.last + 1..it.last)

                            rangeToProcess = sourceRange
                        }
                    }

                    if (rangeToProcess != LongRange.EMPTY)
                    {
                        newSeedRanges.add(rangeToProcess.first + delta..rangeToProcess.last + delta)
                    }
                }


            unprocessedRanges = newUnprocessedRanges
        }

        seedRanges = newSeedRanges.asIterable().union(unprocessedRanges).sortedBy { it.first }.toMutableSet()

        println("On the last line seed ranges are $seedRanges")

        return seedRanges.minOfOrNull { it.first } ?: 0
    }

    // test if implementation meets criteria from the description, like:

    val testInput = readInput("Day05_test")
    checkExpectedValue(35, part1(testInput))
    checkExpectedValue(46, part2(testInput))

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

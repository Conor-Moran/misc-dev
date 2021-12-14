package code.day3

import code.common.BinDigit
import code.common.flipBits
import java.io.File

fun main() {
    doIt("Day 3 Part 1: Test Input", "src/code/day3/test.input", part1)
    doIt("Day 3 Part 1: Real Input", "src/code/day3/part1.input", part1)
    doIt("Day 3 Part 2: Test Input", "src/code/day3/test.input", part2)
    doIt("Day 3 Part 2: Real Input", "src/code/day3/part1.input", part2)
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}

val part1: (List<String>) -> Int = { lines   ->
    val numLength = lines[0].length
    val zeroCounts = MutableList(numLength) {0}
    lines.forEach {
        val numStr = it
        for (i in numStr.indices) {
            if ('0' == numStr[i]) {
                val count = zeroCounts[i]
                zeroCounts[i] = count + 1
            }
        }
    }

    val gammaMcStr = zeroCounts.map { if (it > lines.size / 2) 0.toString() else 1.toString() }
        .reduce { acc, v -> acc + v }

    val gammaMC = Integer.parseInt(gammaMcStr, 2)
    val epsilonLC = flipBits(gammaMC, numLength)

    gammaMC * epsilonLC
}

val part2: (List<String>) -> Int = { lines   ->
    val oxyGenRating = filter(lines, 0, mostCommonBit)
    val co2ScrubRating = filter(lines, 0, leastCommonBit)
    Integer.parseInt(oxyGenRating, 2) * Integer.parseInt(co2ScrubRating, 2)
}

fun filter(numList: List<String>, pos: Int, pred: (numList: List<String>, pos: Int) -> BinDigit): String {
    val tmp = arrayListOf<String>()
    val filtered = arrayListOf<String>()
    tmp.addAll(numList)
    val predDigit = pred(tmp, pos)
    tmp.forEach {
        if (it[pos] == predDigit.char) {
            filtered.add(it)
        }
    }
    return if (filtered.size > 1) {
        filter(filtered, pos + 1, pred)
    } else {
        filtered[0]
    }
}

val mostCommonBit: (List<String>, Int) -> BinDigit = { numList, pos ->
    var zeroCount = 0
    numList.forEach {
        val numStr = it
        if (BinDigit.ZERO.char == numStr[pos]) {
            zeroCount += 1
        }
    }
    val oneCount = numList.size - zeroCount

    if(zeroCount > oneCount) BinDigit.ZERO else BinDigit.ONE
}

val leastCommonBit: (List<String>, Int) -> BinDigit = { numList, pos ->
    mostCommonBit(numList, pos).inv()
}

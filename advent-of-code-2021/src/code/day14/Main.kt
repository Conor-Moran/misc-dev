package code.day14

import java.io.File
import kotlin.math.max
import kotlin.math.min

val doDump = false
fun main() {
    doIt("Day 14 Part 1: Test Input", "src/code/day14/test.input", part1)
    doIt("Day 14 Part 1: Real Input", "src/code/day14/part1.input", part1)
    doIt("Day 14 Part 2: Test Input", "src/code/day14/test.input", part2);
    doIt("Day 14 Part 2: Real Input", "src/code/day14/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Long) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}


val part1: (List<String>) -> Long = { lines   ->

    val input = parse(lines)

    var src = input.initStr
    var result = emptyList<Char>()

    for(i in 1 .. 1) {
        result = mutableListOf<Char>()
        result.add(src[0])
        for(i in 1 until src.length) {
            result.add(input.replacements[src.slice(i-1 .. i)]!!)
            result.add(src[i])
        }

        src = result.joinToString("")
    }

    val counts = mutableMapOf<Char, Long>()
    var minC: Long = Long.MAX_VALUE
    var maxC: Long = Long.MIN_VALUE
    result.forEach {
        counts[it] = counts.getOrDefault(it, 0) + 1
    }

    counts.values.forEach {
        minC = min(it, minC)
        maxC = max(it, maxC)
    }

    maxC - minC
}

typealias Data = MutableMap<Char, MutableMap<Char, Long>>

val part2: (List<String>) -> Long = { lines   ->

    val input = parse(lines)

    var data = newData()

    val src = input.initStr
    for(i in 1 until src.length) {
        val charPair = src.slice(i-1 .. i)!!
        incMapBy(data, 1, charPair[0], charPair[1])
    }

    // dump(data)
    val summary1 = summarise(input.initStr, data)
    for (i in 1..40) {
        data = step(data, input)
    }
    // dump(data)
    val summary = summarise(input.initStr, data)
    var maxV = Long.MIN_VALUE
    var minV = Long.MAX_VALUE
    for (i in summary) {
        maxV = max(maxV, i.value)
        if (i.value != 0L) {
            minV = min(minV, i.value)
        }
    }
    maxV - minV
}

private fun newData(): Data {
    val data = mutableMapOf<Char, MutableMap<Char, Long>>()
    for (i in 'A' until 'Z') {
        val map = mutableMapOf<Char, Long>()
        for (j in 'A' until 'Z') {
            map[j] = 0
        }
        data[i] = map
    }
    return data
}

private fun step(data: Data, input: Input): Data {
    val increments = mutableMapOf<String, Long>()
    for (x in data) {
        for (y in x.value) {
            if (y.value > 0) {
                val charPair = String(charArrayOf(x.key, y.key))
                val newChar = input.replacements[charPair]!!
                val key1 = str(charPair[0], newChar)
                val key2 = str(newChar, charPair[1])
                increments[key1] = increments.getOrDefault(key1, 0L) + y.value
                increments[key2] = increments.getOrDefault(key2, 0L) + y.value
            }
        }
    }
    val newData = newData()
    for (inc in increments) {
        incMapBy(newData, inc.value, inc.key[0], inc.key[1])
    }
    return newData
}

fun str(vararg chars: Char): String {
    return String(chars)
}

fun dump(data: Data) {
    for(i in data) {
        for (j in data[i.key]!!) {
            print(j.value)
            print(',')
        }
        println()
    }
    println()
}

fun summarise(initStr: String, data: Data): MutableMap<Char, Long> {
    val summary = mutableMapOf<Char, Long>()
    for (i in 'A' until 'Z') {
        summary[i] = 0
    }

    for (i in 'A' until 'Z') {
        summary[i] = sumFor(i, initStr, data)
    }
    return summary
}

fun sumFor(c: Char, initStr: String, data: Data): Long {
    var startsWithCount = 0L
    for (j in data[c]!!) {
        startsWithCount += j.value
    }
    val correction = if (initStr.last() == c) 1 else 0
    return startsWithCount + correction
}

fun incMapBy(data: Data, byAmount: Long, i: Char, j: Char) {
    data[i]!!.merge(j, byAmount, Long::plus)
}

fun parse(lines: List<String>): Input {
    val replacements = mutableMapOf<String,Char>()

    for(i in 2 .. lines.lastIndex) {
        val r1 = lines[i].slice(0 .. 0)
        val r2 = lines[i].slice(1 .. 1)
        val yy = lines[i][6]
        replacements[r1.plus(r2)] =  yy
    }
    return Input(lines[0], replacements)
}


class Input(val initStr: String, val replacements: Map<String, Char>)



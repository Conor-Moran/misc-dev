package code.day8

import java.io.File

fun main() {
    doIt("Day 8 Part 1: Test Input", "src/code/day8/test.input", part1)
    doIt("Day 8 Part 1: Real Input", "src/code/day8/part1.input", part1)
    doIt("Day 8 Part 2: Test Input", "src/code/day8/test.input", part2);
    doIt("Day 8 Part 2: Real Input", "src/code/day8/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}

val part1: (List<String>) -> Int = { lines   ->
    val inputs = parse(lines).inputs
/*
 * 1 -> 2
 * 4 -> 4
 * 7 -> 3
 * 8 -> 7
 * 2, 3, 5 -> 5
 * 0, 6, 9 -> 6
 */
    var sum = 0
    for (i in inputs.indices) {
        sum += inputs[i].second.filter { listOf(2, 3, 4, 7).contains(it.length) }.size
    }
    sum
}

val valids = mapOf(0 to "abcefg", 1 to "cf", 2 to "acdeg", 3 to "acdfg", 4 to "bcdf", 5 to "abdfg", 6 to "abdefg",
                7 to "acf", 8 to "abcdefg", 9 to "abcdfg")

val part2: (List<String>) -> Int = { lines ->
    val inputs = parse(lines).inputs

    var sum = 0
    for (i in inputs.indices) {
        val decryptor = decrypt(inputs[i].first)
        var num = ""
        for (elem in inputs[i].second) {
            num = num.plus(decryptor[key(elem)])
        }

        sum += num.toInt()
    }

    sum
}

fun key(str: String): String {
    return String(str.toCharArray().apply { sort() })
}

val decrypt: (List<String>) -> Map<String, String> = { encoded ->
    val digit = MutableList<String>(10) {""}

    digit[7] = encoded.first { it.length == 3 }
    digit[1] = encoded.first { it.length == 2 }
    digit[4] = encoded.first { it.length == 4 }
    digit[8] = encoded.first { it.length == 7 }
    digit[9] = encoded.first { it.length == 6 && containsAll(digit[4], it) }
    digit[0] = encoded.first { it.length == 6 && !containsAll(digit[4], it) && containsAll(digit[1], it) }
    digit[6] = encoded.first { it.length == 6 && it != digit[9] && it != digit[0] }
    digit[3] = encoded.first { it.length == 5 && containsAll(digit[7], it) }
    digit[5] = encoded.first { it.length == 5 && containsAll(it, digit[6]) }
    digit[2] = encoded.first { it.length == 5 && it != digit[5] && it != digit[3] }

    val ret = mutableMapOf<String, String>()
    for (i in digit.indices) {
        ret[key(digit[i])] = i.toString()
    }

    ret
}

fun containsAll(chars: String, str: String): Boolean {
    var ret = true
    for (i in 0 until chars.length) {
        if (!str.contains(chars[i])) {
            ret = false
            break
        }
    }
    return ret
}

val parse: (List<String>) -> Input = { lines ->
    val parsed = MutableList<Pair<List<String>, List<String>>>(lines.size) { Pair(mutableListOf(), mutableListOf()) }
    for (i in lines.indices) {
        val raw = lines[i].split("|").map { it.trim() }
        parsed[i] = Pair(raw[0].split(" "), raw[1].split(" "))
    }

    Input(parsed)
}


class Input(val inputs: List<Pair<List<String>, List<String>>>)

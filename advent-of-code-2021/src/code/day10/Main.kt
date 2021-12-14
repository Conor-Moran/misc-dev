package code.day10

import java.io.File
import java.util.*

fun main() {
    doIt("Day 10 Part 1: Test Input", "src/code/day10/test.input", part1)
    doIt("Day 10 Part 1: Real Input", "src/code/day10/part1.input", part1)
    doIt("Day 10 Part 2: Test Input", "src/code/day10/test.input", part2);
    doIt("Day 10 Part 2: Real Input", "src/code/day10/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Long) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}

val part1: (List<String>) -> Long = { lines   ->
    val illegalChars = mutableListOf<Char>()

    for(line in lines) {
        illegalChars.addAll(doCheck(line))
    }

    var count = 0
    illegalChars.forEach() { c ->
        count += when(c) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            else -> 25137
        }
    }
    count.toLong()
}

val part2: (List<String>) -> Long = { lines   ->
    val completionChars = mutableListOf<List<Char>>()

    for(line in lines) {
        if (doCheck(line).size == 0) {
            completionChars.add(doComplete(line))
        }
    }

    var counts = mutableListOf<Long>()
    completionChars.forEach() { completionChars ->
        counts.add(completionScore(completionChars))
    }
    counts.sort()
    counts[counts.size / 2]
}

fun completionScore(completionChars: List<Char>): Long {
    var count: Long = 0
    completionChars.forEach() { c ->
        count *= 5
        count += when (c) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            else -> 4
        }
    }
    return count
}
fun doCheck(line: String): MutableList<Char> {
    val charStack = Stack<Char>()
    val illegalChars = mutableListOf<Char>()
    for (i in 0 until line.length) {
        val char = line[i]
        if (isOpen(char)) {
            charStack.push(char)
        } else {
            val prevChar = charStack.pop()
            if (!isMatch(char, prevChar)) {
                val expected = matchForOp(prevChar)
                //println("$line: invalid char $char at $i - expected $expected")
                illegalChars.add(char)
                break
            }
        }
    }
    return illegalChars
}

fun doComplete(line: String): MutableList<Char> {
    val charStack = Stack<Char>()
    val completionChars = mutableListOf<Char>()
    for (i in 0 until line.length) {
        val char = line[i]
        if (isOpen(char)) {
            charStack.push(char)
        } else {
            charStack.pop()
        }
    }

    while(charStack.isNotEmpty()) {
        completionChars.add(matchForOp(charStack.pop()))
    }
    return completionChars
}

fun isOpen(char: Char): Boolean {
    val openChars = setOf('(', '{', '<', '[')
    return openChars.contains(char)
}

fun isClose(char: Char): Boolean {
    val closeChars = setOf(')', '}', '>', ']')
    return closeChars.contains(char)
}

fun matchForOp(openingChar: Char): Char{
    val validMatch = mutableMapOf<Char, Char>('(' to ')', '{' to '}', '<' to '>', '[' to ']')
    return validMatch[openingChar]!!
}

fun matchForCl(closingChar: Char): Char{
    val validMatch = mutableMapOf<Char, Char>(')' to '(', '}' to '{', '>' to '<', ']' to '[')
    return validMatch[closingChar]!!
}

fun isMatch(closingChar: Char, openingChar: Char): Boolean {
    return matchForCl(closingChar) == openingChar
}

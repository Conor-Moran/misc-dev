package code.day7

import java.io.File
import kotlin.math.abs

fun main() {
    doIt("Day 7 Part 1: Test Input", "src/code/day7/test.input", part1)
    doIt("Day 7 Part 1: Real Input", "src/code/day7/part1.input", part1)
    doIt("Day 7 Part 2: Test Input", "src/code/day7/test.input", part2);
    doIt("Day 7 Part 2: Real Input", "src/code/day7/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Long) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}

val part1: (List<String>) -> Long = { lines   ->
    val input = parse(lines)
    val minNum = input.nums.minOrNull()!!
    val maxNum = input.nums.maxOrNull()!!
    var minCost = Long.MAX_VALUE

    for (i in minNum .. maxNum) {
        val costI = cost(input.nums, i)
        //println("$i - $costI")
        if (costI < minCost) {
            minCost = costI
        }
    }

    minCost
}

val part2: (List<String>) -> Long = { lines   ->
    val input = parse(lines)
    val minNum = input.nums.minOrNull()!!
    val maxNum = input.nums.maxOrNull()!!
    var minCost = Long.MAX_VALUE

    for (i in minNum .. maxNum) {
        val costI = cost2(input.nums, i)
        //println("$i - $costI")
        if (costI < minCost) {
            minCost = costI
        }
    }

    minCost
}

val parse: (List<String>) -> Input = { lines ->
    val nums = lines[0].split(",").map { it.toInt() }
    Input(nums)
}

fun cost(nums: List<Int>, pos: Int): Long {
    var sum: Long = 0
    for (element in nums) {
        sum += abs(element - pos)
    }
    return sum
}

fun cost2(nums: List<Int>, pos: Int): Long {
    var sum: Long = 0
    for (element in nums) {
        val dist = abs(element - pos)
        sum += (dist * dist + dist)/2
    }
    return sum
}


class Input(val nums: List<Int>)


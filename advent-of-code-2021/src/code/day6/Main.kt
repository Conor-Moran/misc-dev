package code.day6

import java.io.File

fun main() {
    doIt("Day 6 Part 1: Test Input", "src/code/day6/test.input", part1);
    doIt("Day 6 Part 1: Real Input", "src/code/day6/part1.input", part1);
    doIt("Day 6 Part 2: Test Input", "src/code/day6/test.input", part2);
    doIt("Day 6 Part 2: Real Input", "src/code/day6/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Long) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) };

    println(String.format("%s: Ans: %d", msg , calc(lines)));
}

val part1: (List<String>) -> Long = { lines   ->
    val numDays = 80;
    val input = parse(lines)
    val fishList = MutableList<Long>(9) {0};
    for (i in 0 .. 8) {
        fishList[i] = 0
    }
    input.nums.forEach() {
        fishList[it] = fishList[it]!!.inc()
    }

    for (i in 0 until numDays) {
        tick(fishList)
    }
    fishList.sum()
}

val part2: (List<String>) -> Long = { lines   ->
    val numDays = 256;
    val input = parse(lines)
    val fishList = MutableList<Long>(9) {0};
    for (i in 0 .. 8) {
        fishList[i] = 0
    }
    input.nums.forEach() {
        fishList[it] = fishList[it]!!.inc()
    }

    for (i in 0 until numDays) {
        tick(fishList)
    }
    fishList.sum()
}

val tick: (MutableList<Long>) -> Unit = { fish ->
    val zeros = fish[0]!!
    for (i in 0 until 8) {
        fish[i] = fish[i+1]!!
    }
    fish[8] = zeros
    fish[6] += zeros
}

val parse: (List<String>) -> Input = { lines ->
    val nums = lines[0].split(",").map { Integer.parseInt(it) }

    Input(nums)
}

class Input(val nums: List<Int>) {}


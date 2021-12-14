package code.day1

import java.io.File

fun main() {
    doIt("Day1, Part 1: Test Input", "src/code/day1/test.input", part1);
    doIt("Day1, Part 1: Real Input", "src/code/day1/part1.input", part1);
    doIt("Day1, Part 2: Test Input", "src/code/day1/test.input", part2);
    doIt("Day1, Part 2: Real Input", "src/code/day1/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<Int>) -> Int) {
    println(String.format("%s: %d", msg , calc(readInput(input))));
}

fun readInput(input: String): List<Int> {
    val nums = arrayListOf<Int>()
    File(input).forEachLine { nums.add(Integer.parseInt(it)) };
    return nums
}


val part2: (List<Int>) -> Int = { nums ->
    var increases = 0;

    for (i in 3 until nums.size) {
        val n1 = nums.slice(i-3 until i ).sum();
        val n2 = nums.slice(i-2 .. i).sum();
        if (n2 > n1) {
            increases += 1;
        }
    }

    increases;
}

val part1: (List<Int>) -> Int = { nums ->
    var increases = 0;

    for (i in 1 until nums.size) {
        var prev = nums[i-1];
        var current = nums[i];
        if (current > prev) {
            increases += 1;
        }
    }

    increases;
}


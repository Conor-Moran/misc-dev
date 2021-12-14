package code.day2

import java.io.File

fun main() {
    doIt("Day 2 Part 1: Test Input", "src/code/day2/test.input", part1);
    doIt("Day 2 Part 1: Real Input", "src/code/day2/part1.input", part1);
    doIt("Day 2 Part 2: Test Input", "src/code/day2/test.input", part2);
    doIt("Day 2 Part 2: Real Input", "src/code/day2/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) };

    println(String.format("%s: Ans: %d", msg , calc(lines)));
}

val part1: (List<String>) -> Int = { lines   ->
    val tots = mutableMapOf<String, Int>();
    lines.forEach {
        val cmd = parse(it);
        val tot = tots.computeIfAbsent(cmd.first) { 0 };
        tots[cmd.first] = tot + cmd.second;
    };

    val fwd = tots.getOrDefault("forward",0);
    val depth = tots.getOrDefault("down", 0) - tots.getOrDefault("up", 0);

    fwd * depth;
}

fun parse(line: String): Pair<String, Int> {
    val tokens = line.split(" ");
    return Pair<String, Int>(tokens[0], Integer.parseInt(tokens[1]));
}

val part2: (List<String>) -> Int = { lines   ->
    var aim = 0;
    var horiz = 0;
    var depth = 0;
    lines.forEach {
        val cmd = parse(it);
        when(cmd.first) {
            "forward" -> { horiz += cmd.second; depth += aim * cmd.second }
            "up" -> { aim -= cmd.second; }
            "down" -> { aim += cmd.second; }
        }
    };

    depth * horiz;
}

package code.day12

import code.common.isLowercase
import java.io.File

fun main() {
    doIt("Day 12 Part 1: Test Input", "src/code/day12/test.input", part1)
    doIt("Day 12 Part 1: Real Input", "src/code/day12/part1.input", part1)
    doIt("Day 12 Part 2: Test Input", "src/code/day12/test.input", part2);
    doIt("Day 12 Part 2: Real Input", "src/code/day12/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}

val part1: (List<String>) -> Int = { lines   ->
    val map = parse(lines).map
    val paths = mutableListOf(mutableListOf("start"))
    buildPaths(paths, map);
    dump(paths)
    paths.size
}

val part2: (List<String>) -> Int = { lines   ->
    val map = parse(lines).map
    val paths = mutableListOf(mutableListOf("start"))
    buildPaths2(paths, map);
    dump(paths)
    paths.size
}

private fun dump(paths: MutableList<MutableList<String>>) {
    return
    for (path in paths) {
        println(path.joinToString(", "))
    }
}

fun buildPaths(paths: MutableList<MutableList<String>>, map: Map<String, List<String>>) {
    val allPaths = mutableListOf<MutableList<String>>()
    allPaths.addAll(paths)
    paths.removeAll() { true }
    var allDone = true
    for (path in allPaths) {
        val lastFragment = path.last()
        if (lastFragment == "end") {
            paths.add(path)
            continue
        }
        allDone = false;
        val dests = map.getOrDefault(lastFragment, emptyList())
        for (dest in dests) {
            if (!dest.isLowercase() || !path.contains(dest)) {
                addPath(path, dest, paths)
            }
        }
    }
    if (!allDone) buildPaths(paths, map)
}

fun buildPaths2(paths: MutableList<MutableList<String>>, map: Map<String, List<String>>) {
    val allPaths = mutableListOf<MutableList<String>>()
    allPaths.addAll(paths)
    paths.removeAll() { true }
    var allDone = true

    for (path in allPaths) {
        val lastFragment = path.last()
        val canDupeSmall = !hasDupeSmall(path)
        if (lastFragment == "end") {
            paths.add(path)
            continue
        }
        allDone = false;
        val dests = map.getOrDefault(lastFragment, emptyList())
        for (dest in dests) {
            if (!dest.isLowercase()) {
                addPath(path, dest, paths)
            } else {
                if (dest == "start") continue
                if (path.contains(dest)) {
                    if (canDupeSmall) {
                        addPath(path, dest, paths)
                    }
                } else {
                    addPath(path, dest, paths)
                }
            }
        }
    }
    if (!allDone) buildPaths2(paths, map)
}

private fun addPath(
    pathBase: MutableList<String>,
    dest: String,
    paths: MutableList<MutableList<String>>
) {
    val newPath = mutableListOf<String>()
    newPath.addAll(pathBase)
    newPath.add(dest)
    paths.add(newPath)
}

fun hasDupeSmall(path: List<String>): Boolean {
    val uniqSmall = mutableSetOf<String>()
    path.filter { it.isLowercase() }.forEach {
        if (uniqSmall.contains(it)) return true
        uniqSmall.add(it)
    }
    return false
}
val parse: (List<String>) -> Input = { lines ->
    val parsed = mutableMapOf<String, MutableList<String>>()
    lines.forEach() {
        val tokens = it.split("-")
        parsed.getOrPut(tokens[0]) { mutableListOf<String>() }.add(tokens[1])
        parsed.getOrPut(tokens[1]) { mutableListOf<String>() }.add(tokens[0])
    }
    Input(parsed)
}

class Input(val map: Map<String, List<String>>)

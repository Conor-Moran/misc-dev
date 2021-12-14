package code.day9

import java.io.File

fun main() {
    doIt("Day 9 Part 1: Test Input", "src/code/day9/test.input", part1)
    doIt("Day 9 Part 1: Real Input", "src/code/day9/part1.input", part1)
    doIt("Day 9 Part 2: Test Input", "src/code/day9/test.input", part2);
    doIt("Day 9 Part 2: Real Input", "src/code/day9/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val grid = arrayListOf<String>()
    File(input).forEachLine { grid.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(grid)))
}

val part1: (List<String>) -> Int = { lines   ->
    val inputs = parse(lines).grid
    val lowPts = mutableListOf<Int>()

    for(i in inputs.indices) {
        for(j in inputs[i].indices) {
            if (isLow(i, j, inputs)) {
                lowPts.add(inputs[i][j])
            }
        }
    }

    lowPts.sum() + lowPts.size
}

val part2: (List<String>) -> Int = { lines   ->
    val grid = parse(lines).grid
    val lowCoords = mutableListOf<Pair<Int,Int>>()
    val allBasinValues = mutableListOf<List<Int>>()

    for(i in grid.indices) {
        for(j in grid[i].indices) {
            if (isLow(i, j, grid)) {
                lowCoords.add(Pair(i, j))
            }
        }
    }

    for (lowCoord in lowCoords) {
        val basinValues = mutableListOf<Int>()
        allBasinValues.add(basinValues)
        addBasin(lowCoord, grid, basinValues);
    }

    allBasinValues.sortByDescending { it.size }

    allBasinValues[0].size * allBasinValues[1].size * allBasinValues[2].size
}

fun addBasin(lowCoord: Pair<Int, Int>, grid: Array<IntArray>, basinValues: MutableList<Int>) {
    addBasin(lowCoord.first, lowCoord.second, grid, basinValues, mutableListOf<Pair<Int,Int>>())
}

fun addBasin(i: Int, j: Int, grid: Array<IntArray>, basinValues: MutableList<Int>, visited: MutableList<Pair<Int,Int>>) {
    val coord = Pair(i, j)
    if (visited.contains(coord)) {
        return
    } else {
        basinValues.add(getValue(i, j, grid))
        visited.add(coord)
    }
    if (upValue(i, j, grid) < 9) {
        addBasin(i - 1, j, grid, basinValues, visited);
    }
    if (downValue(i, j, grid) < 9) {
        addBasin(i + 1, j, grid, basinValues, visited);
    }
    if (leftValue(i, j, grid) < 9) {
        addBasin(i, j - 1, grid, basinValues, visited);
    }
    if (rightValue(i, j, grid) < 9) {
        addBasin(i, j + 1, grid, basinValues, visited);
    }
}


val parse: (List<String>) -> Input = { lines ->
    val parsed = Array(lines.size) { IntArray(lines[0].length) }

    for (i in lines.indices) {
        val nums = lines[i].toCharArray()
        for (j in nums.indices) {
            parsed[i][j] = nums[j].toString().toInt()
        }
    }

    Input(parsed)
}

fun isLow(i: Int, j: Int, grid: Array<IntArray>): Boolean {
    val item = grid[i][j]

    val up = upValue(i, j, grid)
    val down = downValue(i, j, grid)
    val left = leftValue(i, j, grid)
    val right = rightValue(i, j, grid)

    return item < up && item < down && item < left && item < right
}

fun upValue(i: Int, j: Int, grid: Array<IntArray>): Int {
    return getValue(i-1, j, grid)
}

fun downValue(i: Int, j: Int, grid: Array<IntArray>): Int {
    return getValue(i+1, j, grid)
}

fun leftValue(i: Int, j: Int, grid: Array<IntArray>): Int {
    return getValue(i, j-1, grid)
}

fun rightValue(i: Int, j: Int, grid: Array<IntArray>): Int {
    return getValue(i, j+1, grid)
}

fun getValue(i: Int, j: Int, grid: Array<IntArray>): Int {
    val dimI = grid.size
    val dimJ = grid[0].size
    var value = Int.MAX_VALUE

    if (i in 0 until dimI
        && j in 0 until dimJ) {
        value = grid[i][j]
    }
    return value
}

class Input(val grid: Array<IntArray>)

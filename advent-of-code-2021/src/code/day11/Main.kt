package code.day11

import java.io.File

fun main() {
    doIt("Day 11 Part 1: Test Input", "src/code/day11/test.input", part1)
    doIt("Day 11 Part 1: Real Input", "src/code/day11/part1.input", part1)
    doIt("Day 11 Part 2: Test Input", "src/code/day11/test.input", part2);
    doIt("Day 11 Part 2: Real Input", "src/code/day11/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val grid = arrayListOf<String>()
    File(input).forEachLine { grid.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(grid)))
}

val part1: (List<String>) -> Int = { lines   ->
    val inputs = parse(lines).grid
    var flashes = 0

    for (i in 1.. 100) {
        flashes += step(inputs)
        //dump(inputs)
    }

    flashes
}

val part2: (List<String>) -> Int = { lines   ->
    val inputs = parse(lines).grid
    val size = inputs.size * inputs[0].size
    var step = 1
    while (true) {
        if (step(inputs) == size) break
        step++
    }

    step
}

val parse: (List<String>) -> Input = { lines ->
    val parsed = Array(lines.size) { Array<Cell>(lines[0].length) { Cell(0, false, false) } }

    for (i in lines.indices) {
        val nums = lines[i].toCharArray()
        for (j in nums.indices) {
            parsed[i][j].num = nums[j].toString().toInt()
        }
    }

    Input(parsed)
}

fun step(grid: Array<Array<Cell>>): Int {
    clearFrozen(grid)
    incAll(grid)
    return flash(grid)
}

fun flash(grid: Array<Array<Cell>>): Int {
    var flashCount = 0
    clearFlash(grid)

    for(i in grid.indices) {
        for(j in grid[0].indices) {
            if (grid[i][j].num > 9) {
                grid[i][j].num = 0
                grid[i][j].isFlashed = true
                grid[i][j].isFrozen = true
                flashCount++
            }
        }
    }
    incFlashedNeighbors(grid)

    if (flashCount > 0) {
        flashCount += flash(grid)
    }

    return flashCount
}

fun clearFlash(grid: Array<Array<Cell>>) {
    for(i in grid.indices) {
        for (j in grid[0].indices) {
            grid[i][j].isFlashed = false;
        }
    }
}

fun clearFrozen(grid: Array<Array<Cell>>) {
    for(i in grid.indices) {
        for (j in grid[0].indices) {
            grid[i][j].isFrozen = false;
        }
    }
}

fun dump(grid: Array<Array<Cell>>) {
    for(i in grid.indices) {
        for(j in grid[0].indices) {
            print(grid[i][j])
        }
        println()
    }
    println()
}
fun incAll(grid: Array<Array<Cell>>) {
    for(i in grid.indices) {
        for(j in grid[0].indices) {
            grid[i][j].num++
        }
    }
}

fun incFlashedNeighbors(grid: Array<Array<Cell>>) {
    for(i in grid.indices) {
        for(j in grid[0].indices) {
            if (grid[i][j].isFlashed) incNeighbors(i, j , grid)
        }
    }
}

fun incNeighbors(i: Int, j: Int, grid: Array<Array<Cell>>) {
    inc(i-1, j-1, grid)
    inc(i-1, j, grid)
    inc(i-1, j+1, grid)
    inc(i, j-1, grid)
    inc(i, j+1, grid)
    inc(i+1, j-1, grid)
    inc(i+1, j, grid)
    inc(i+1, j+1, grid)
}

fun inc(i: Int, j: Int, grid: Array<Array<Cell>>) {
    val dimI = grid.size
    val dimJ = grid[0].size

    if (i in 0 until dimI
        && j in 0 until dimJ) {
        val cell = grid[i][j]
        if (!cell.isFrozen) cell.num++
    }
}

class Cell(var num: Int, var isFlashed: Boolean, var isFrozen: Boolean) {
    override fun toString(): String {
        return "$num"
    }
}

class Input(val grid: Array<Array<Cell>>)

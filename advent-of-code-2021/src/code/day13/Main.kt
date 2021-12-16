package code.day13

import java.io.File
import java.lang.Integer.max
import kotlin.math.min

val doDump = false
fun main() {
    doIt("Day 13 Part 1: Test Input", "src/code/day13/test.input", part1)
    doIt("Day 13 Part 1: Real Input", "src/code/day13/part1.input", part1)
    doIt("Day 13 Part 2: Test Input", "src/code/day13/test.input", part2);
    doIt("Day 13 Part 2: Real Input", "src/code/day13/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> String) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %s", msg , calc(lines)))
}

typealias Grid = Array<Array<Char>>

val part1: (List<String>) -> String = { lines   ->
    val grid = solve(lines)
    countDots(grid).toString()
}

val part2: (List<String>) -> String = { lines   ->
    val grid = solve(lines, false)
    toString(grid)
}


fun solve(lines: List<String>, limitOne: Boolean = true): Grid {
    val input = parse(lines)
    var grid = Array(input.max.x + 1) { Array<Char>(input.max.y + 1) { '.' } }

    for (p in input.points) {
        grid[p.x][p.y] = '#'
    }

    var mList = mutableListOf<Point>()
    for (p in input.points) {
        mList.add(p)
    }

    mList.sortBy { it.y }

    for (f in input.folds) {
        grid = fold(grid, f)
        if (limitOne) break
    }

    return grid
}

private fun countDots(grid: Grid): Int {
    var count = 0
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            if (grid[i][j] == '#') count++
        }
    }
    return count
}

fun fold(grid: Grid, fold: Fold): Grid {
    return when (fold.axis) {
        'x' -> foldLeft(grid, fold.pos)
        else -> foldUp(grid, fold.pos)
    }
}

fun foldUp(grid: Grid, splitPos: Int): Grid {

    val foldedGridLen = max(splitPos, grid[0].lastIndex - splitPos)
    val foldedGrid = Array(grid.size) { Array<Char>(foldedGridLen) { '.' } }

    val topLen = splitPos

    for (i in grid.indices ) {
        val head = grid[i].slice(0 until topLen)
        val topOffset = foldedGridLen - head.size
        for (j in head.indices) {
            foldedGrid[i][j + topOffset] = grid[i][j]
        }
    }

    for (i in grid.indices ) {
        val tail = grid[i].slice(topLen + 1 until grid[0].size)
        val tailOffset = foldedGridLen - tail.size
        for(j in tail.indices.reversed()) {
            if (tail[j] == '#') {
                foldedGrid[i][((tail.size - 1) - j) + tailOffset] = '#'
            }
        }
    }

    return foldedGrid
}

fun foldLeft(grid: Grid, splitPos: Int): Grid {

    val foldedGridLen = max(splitPos, grid.lastIndex - splitPos)
    val foldedGrid = Array(foldedGridLen) { Array<Char>(grid[0].size) { '.' } }


    val topLen = splitPos

    val head = grid.slice(0 until topLen)
    val tail = grid.slice(topLen + 1 until grid.size)


    val topOffset = foldedGridLen - head.size
    for (i in head.indices ) {
        for (j in head[0].indices) {
            foldedGrid[i + topOffset][j] = grid[i][j]
        }
    }

    val tailOffset = foldedGridLen - tail.size
    for (i in tail.indices.reversed() ) {
        for(j in tail[0].indices) {
            if (tail[i][j]=='#') {
                foldedGrid[((tail.size - 1) - i) + tailOffset][j] = '#'
            }
        }
    }

    return foldedGrid

}

private fun toString(grid: Grid): String {
    val sb =  StringBuilder()
    sb.append(System.lineSeparator())
    for(j in grid[0].indices) {
        for (i in grid.indices) {
            sb.append(grid[i][j])
        }
        sb.append(System.lineSeparator())
    }
    sb.append(System.lineSeparator())
    return sb.toString()
}

private fun dump(grid: Grid) {
    if (!doDump) return
    for(j in grid[0].indices) {
        for (i in grid.indices) {
            print(grid[i][j])
        }
        println()
    }
}

val parse: (List<String>) -> Input = { lines ->
    val points = mutableListOf<Point>()
    val folds = mutableListOf<Fold>()
    var minX = Int.MAX_VALUE
    var minY = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE

    lines.filter { !it.isNullOrBlank() }.forEach {
        if (!it.startsWith("fold along")) {
            val coords = it.split(",").map { v -> v.toInt() }
            val point = Point(coords[0], coords[1])
            points.add(point)
            minX = min(minX, point.x)
            minY = min(minY, point.y)
            maxX = max(maxX, point.x)
            maxY = max(maxY, point.y)
        } else {
            val rawFolds = it.substringAfter("fold along ").split("=")
            folds.add(Fold(rawFolds[0][0], rawFolds[1].toInt()))
        }
    }

    Input(points.toTypedArray(), folds.toTypedArray(), Point(minX, minY), Point(maxX, maxY))
}

class Point(val x: Int, val y: Int)
class Fold(val axis: Char, val pos: Int)

class Input(val points: Array<Point>, val folds: Array<Fold>, val min: Point, val max: Point)

package code.day13a

import java.io.File
import java.lang.Integer.max
import kotlin.math.min

fun main() {
    doIt("Day 13a Part 1: Test Input", "src/code/day13a/test.input", part1)
    doIt("Day 13a Part 2: Real Input", "src/code/day13a/part1.input", part1);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> String) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %s", msg , calc(lines)))
}

val part1: (List<String>) -> String = { lines ->
    val input = parse(lines)
    val points = mutableSetOf<Point>()
    points.addAll(input.points)

    for (fold in input.folds) {
        fold(points, fold)
    }

    toString(grid(points))
}

fun grid(points: Set<Point>): Grid {
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE

    for (point in points) {
        maxX = max(maxX, point.x)
        maxY = max(maxY, point.y)
    }

    val g = Array(maxX + 1) { Array<Char>(maxY + 1) { '.' } }
    for (point in points) {
        g[point.x][point.y] = '#'
    }
    return g
}

fun fold(points: MutableSet<Point>, fold: Fold) {
    return when (fold.axis) {
        'x' -> foldLeft(points, fold.pos)
        else -> foldUp(points, fold.pos)
    }
}

fun foldUp(points: MutableSet<Point>, splitPos: Int) {
    val shiftedPoints = mutableSetOf<Point>()
    for(point in points) {
        if (point.y > splitPos) {
            shiftedPoints.add(point)
        }
    }

    points.removeAll(shiftedPoints)

    for (point in shiftedPoints) {
        point.y = splitPos - (point.y - splitPos)
        if (!points.contains(point)) {
            points.add(point)
        }
    }
}

fun foldLeft(points: MutableSet<Point>, splitPos: Int) {
    val shiftedPoints = mutableSetOf<Point>()
    for(point in points) {
        if (point.x > splitPos) {
            shiftedPoints.add(point)
        }
    }

    points.removeAll(shiftedPoints)

    for (point in shiftedPoints) {
        point.x = splitPos - (point.x - splitPos)
        if (!points.contains(point)) {
            points.add(point)
        }
    }
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

val parse: (List<String>) -> Input = { lines ->
    val points = mutableListOf<Point>()
    val folds = mutableListOf<Fold>()

    lines.filter { !it.isNullOrBlank() }.forEach {
        if (!it.startsWith("fold along")) {
            val coords = it.split(",").map { v -> v.toInt() }
            val point = Point(coords[0], coords[1])
            points.add(point)
        } else {
            val rawFolds = it.substringAfter("fold along ").split("=")
            folds.add(Fold(rawFolds[0][0], rawFolds[1].toInt()))
        }
    }

    Input(points.toTypedArray(), folds.toTypedArray())
}

typealias Grid = Array<Array<Char>>

data class Point(var x: Int, var y: Int)
class Fold(val axis: Char, val pos: Int)

class Input(val points: Array<Point>, val folds: Array<Fold>)

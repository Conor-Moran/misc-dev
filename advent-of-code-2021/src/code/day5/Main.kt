package code.day5

import java.io.File
import java.lang.Integer.max
import kotlin.math.abs

fun main() {
    doIt("Day 5 Part 1: Test Input", "src/code/day5/test.input", part1);
    doIt("Day 5 Part 1: Real Input", "src/code/day5/part1.input", part1);
    doIt("Day 5 Part 2: Test Input", "src/code/day5/test.input", part2);
    doIt("Day 5 Part 2: Real Input", "src/code/day5/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) };

    println(String.format("%s: Ans: %d", msg , calc(lines)));
}

val part1: (List<String>) -> Int = { lines   ->
    val input = parse(lines)
    val grid = Grid(input.maxX + 1, input.maxY + 1)
    grid.draw(input.lines)
    grid.show()
    grid.count()
}

val part2: (List<String>) -> Int = { lines   ->
    val input = parse(lines)
    val grid = Grid(input.maxX + 1, input.maxY + 1, false)
    grid.draw(input.lines)
    grid.count()
}

val parse: (List<String>) -> Input = { lines ->
    val endpoints = mutableListOf<Line>();
    var maxX: Int = Int.MIN_VALUE
    var maxY: Int = Int.MIN_VALUE


    lines.forEach() { line ->
        val points = line.split(Regex(" -> ")).map {
            val coords = it.split(",")
            Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]))
        }
        endpoints.add(Line(points[0], points[1]))
        maxX = max(maxX, max(points[0].x, points[1].x))
        maxY = max(maxY, max(points[0].y, points[1].y))
    }

    Input(endpoints, maxX, maxY)
}

class Input(val lines: List<Line>, val maxX: Int, val maxY: Int) {}

class Point(val x: Int, val y: Int) {
    override fun toString(): String {
        return "Point($x, $y)"
    }
}

class Line(val p1: Point, val p2: Point) {
    override fun toString(): String {
        return "Line($p1, $p2)"
    }
}

class Grid(val xLen: Int, val yLen: Int, var simple: Boolean = true): ArrayList<MutableList<Int>>(xLen) {

    init {
        fill()
    }

    private fun fill() {
        for (i in 0 until xLen) {
            this.add(MutableList(yLen) { 0 })
        }
    }

    fun inc(p: Point) {
        inc(p.x, p.y)
    }

    fun inc(i: Int, j: Int) {
        this[i][j]++
    }

    fun draw(line: Line) {

        val dxi = line.p2.x - line.p1.x
        val dyi = line.p2.y - line.p1.y
        var dx = decDelta(dxi)
        var dy = decDelta(dyi)
        if (simple && isAngled(dx, dy)) return
        inc(line.p2)
        inc(line.p1)
        while (abs(dx) > 0 || abs(dy) > 0) {
            inc(line.p1.x + dx, line.p1.y + dy)
            dx = decDelta(dx)
            dy = decDelta(dy)
        }
    }

    fun isAngled(dx: Int, dy: Int): Boolean {
        return dx != 0 && dy != 0
    }

    private fun decDelta(delta: Int): Int {
        return when {
            delta > 0 -> delta - 1
            delta < 0 ->  delta + 1
            else -> 0
        }
    }

    fun draw(lines: List<Line>) {
        lines.forEach() { draw(it) }
        //draw(lines[0])
    }

    fun count(): Int {
        var cnt = 0;
        for (x in 0 until xLen) {
            for (y in 0 until yLen) {
                if(this[x][y] >= 2) cnt++
            }
        }
        return cnt
    }

    fun show() {
        if (xLen > 50) return
        for (x in 0 until xLen) {
            for (y in 0 until yLen) {
                print(this[x][y])
            }
            println()
        }
    }
}

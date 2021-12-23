package code.day15

import code.common.twoDimIterate
import java.io.File

fun main() {
    doIt("Day 15 Part 1: Test Input", "src/code/day15/test.input", part1)
    doIt("Day 15 Part 1: Real Input", "src/code/day15/part1.input", part1)
    doIt("Day 15 Part 2: Test Input", "src/code/day15/test.input", part2)
    doIt("Day 15 Part 2: Real Input", "src/code/day15/part1.input", part2)
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Long) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) }

    println(String.format("%s: Ans: %d", msg , calc(lines)))
}

val part1: (List<String>) -> Long = { lines   ->
    val data = parse(lines)

    //dump(data)

    findMinPathDistDijkstra(data, Point(0, 0), Point(data.lastIndex, data[0].lastIndex))
}

val part2: (List<String>) -> Long = { lines   ->
    val origData = parse(lines)

    //dump(data)
    val data = tile(origData)

    findMinPathDistDijkstra(data, Point(0, 0), Point(data.lastIndex, data[0].lastIndex))
}

fun tile(data: Data): Data {
    val magFactor = 5
    val tiledData = Array<Array<Int>>(magFactor * data.size) { Array<Int>(magFactor * data[0].size) {0} }

    var dataToUse = data
    for (i in 0 until 5) {
        for (j in 0 until 5) {
            twoDimIterate(dataToUse) { v, _, ii, jj ->
                tiledData[i * dataToUse.size + ii][j * dataToUse[0].size + jj] = v
            }
            dataToUse = incTile(dataToUse)
        }
        dataToUse = data
        for(x in 0 until i + 1) dataToUse = incTile(dataToUse)
    }

    return tiledData
}

fun incTile(data: Data): Data {
    val incData = Array<Array<Int>>(data.size) { Array<Int>(data[0].size) {0} }
    twoDimIterate(data) { v, _, i, j ->
        incData[i][j] = if (v == 9) 1 else v + 1
    }
    return incData
}

/*
 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 * Notes:
 * - Dropped prev, no need to know the path
 * - Could make this more OO
 * - Added activeDists as performance was poor when we didn't prune removed vertices
 */
fun findMinPathDistDijkstra(data: Array<Array<Int>>, from: Point, to: Point): Long {
    val allDists = mutableMapOf<Point, Long>()
    val activeDists = mutableMapOf<Point, Long>()
    val vertices = mutableSetOf<Point>()
    twoDimIterate(data) { _, _, i, j ->
        val v = Point(i, j)
        vertices.add(v)
    }
    activeDists[from] = 0L
    allDists[from] = 0L
    while(vertices.isNotEmpty()) {
        val u = vertWithMinDistance(activeDists, vertices)
        val distU = allDists[u]!!
        vertices.remove(u)
        val neighbours = neighbours(u, vertices)

        if (u == to) {
            break
        }

        for (neighbour in neighbours) {
            val alt = distU + data[neighbour.x][neighbour.y]
            if (alt < activeDists.getOrDefault(neighbour, Long.MAX_VALUE)) {
                activeDists[neighbour] = alt
                allDists[neighbour] = alt
            }
        }
        //if (vertices.size % 100 == 0) println(vertices.size)
    }
    return allDists[to]!!
}

fun neighbours(p: Point, vertices: Set<Point>): Set<Point> {
    val ret = mutableSetOf<Point>()
    if (p.x > 0) {
        val left = Point(p.x - 1, p.y)
        if (vertices.contains(left)) {
            ret.add(left)
        }
    }
    if (p.y > 0) {
        val up = Point(p.x, p.y - 1)
        if (vertices.contains(up)) {
            ret.add(up)
        }
    }
    val right = Point(p.x + 1, p.y)
    if (vertices.contains(right)) {
        ret.add(right)
    }
    val down = Point(p.x, p.y + 1)
    if (vertices.contains(down)) {
        ret.add(down)
    }
    return ret
}

fun vertWithMinDistance(distMap: MutableMap<Point, Long>, vertices: Set<Point>): Point {
    val toRemove = mutableSetOf<Point>()
    var minDist = Long.MAX_VALUE
    var minSoFar: Point? = null
    for (e in distMap) {
        if (!vertices.contains(e.key)) {
            toRemove.add(e.key)
        }
        if (e.value < minDist) {
            minSoFar = e.key
            minDist = e.value
        }
    }
    toRemove.forEach {
        distMap.remove(it)
    }
    return minSoFar!!
}

fun parse(lines: List<String>): Array<Array<Int>> {
    val data = Array(lines.size) { Array( lines[0].length) { 0 } }

    lines.forEachIndexed { i, str ->
        for(j in str.indices) {
            data[i][j] = str[j].digitToInt()
        }
    }

    return data
}

fun dump(data: Data) {
    twoDimIterate(data) { i, isNewLine, _, _ ->
        if (isNewLine) println()
        print(i)
    }
    println()
    return
}

typealias Data = Array<Array<Int>>

data class Point(val x: Int, val y: Int)
class Input(val x: String, val replacements: Map<String, Char>)



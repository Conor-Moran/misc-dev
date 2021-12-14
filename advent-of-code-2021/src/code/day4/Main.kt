package code.day4

import code.common.getOrPut
import java.io.File

fun main() {
    doIt("Day 4 Part 1: Test Input", "src/code/day4/test.input", part1);
    doIt("Day 4 Part 1: Real Input", "src/code/day4/part1.input", part1);
    doIt("Day 4 Part 2: Test Input", "src/code/day4/test.input", part2);
    doIt("Day 4 Part 2: Real Input", "src/code/day4/part1.input", part2);
}

fun doIt(msg: String, input: String, calc: (nums: List<String>) -> Int) {
    val lines = arrayListOf<String>()
    File(input).forEachLine { lines.add(it) };

    println(String.format("%s: Ans: %d", msg , calc(lines)));
}

val part1: (List<String>) -> Int = { lines   ->
    val parsed = parse(lines)
    playPt1(parsed.first, parsed.second)
}

val part2: (List<String>) -> Int = { lines   ->
    val parsed = parse(lines)
    playPt2(parsed.first, parsed.second)
}


val parse: (List<String>) -> Pair<List<Board>, List<Int>> = { lines ->
    val firstLine = lines[0];
    val callNums = firstLine.split(",").map { Integer.parseInt(it) }
    val boards = mutableListOf<Board>()
    var curBoardIndex = 0

    var rowCounter = 0;
    for (i  in 1 until lines.size) {
        if (!lines[i].isNullOrBlank()) {
            rowCounter = if (rowCounter < 4) rowCounter + 1 else 0
            val curBoard = boards.getOrPut(curBoardIndex, Board(5))
            curBoard.parseRow(lines[i])
            if (rowCounter == 0) {
                curBoardIndex++
            };
        }
    }
    Pair<List<Board,>, List<Int>>(boards, callNums)
}

val playPt1: (List<Board>, List<Int>) -> Int = { boards, callNums ->
    var out: Pair<Board?, Int> = play(boards, callNums, true);

    if (out.first != null) out.first!!.sumUnmarked() * out.second else 0
}

val playPt2: (List<Board>, List<Int>) -> Int = { boards, callNums ->
    var out: Pair<Board?, Int> = play(boards, callNums, false);

    if (out.first != null) out.first!!.sumUnmarked() * out.second else 0
}

fun play(boards: List<Board>, callNums: List<Int>, firstWin: Boolean): Pair<Board?, Int> {
    var winningBoard: Board? = null;
    var winningNum: Int? = null;

    loop@ for (num in callNums) {
        for (board in boards) {
            board.sendNum(num)
            if (!board.isCounted && board.isWinning()) {
                winningBoard = board;
                winningNum = num
                board.setCounted()

                if (firstWin) break@loop
            }
        }
    }
    return Pair(winningBoard, winningNum ?: 0)
}

class Cell(val intValue: Int, var marked: Boolean) {
    fun mark() {
        this.marked = true;
    }

    override fun toString(): String {
        return "Cell($intValue, $marked)"
    }

    fun sendNum(num: Int) {
        if (intValue == num) {
            mark()
        }
    }
}
class Seq(data: List<Cell>): ArrayList<Cell>() {
    fun sendNum(num: Int) {
        for (cell in this) {
            cell.sendNum(num)
        }
    }

    init {
        this.addAll(data)
    }

    fun isWinning(): Boolean {
        var isWinning = true;
        for (cell in this) {
            if (!cell.marked) {
                isWinning = false;
                break;
            }
        }
        return isWinning
    }
}

class Board(size: Int): ArrayList<Seq>(size) {
    var isFinished = false;
    var isCounted = false;
    var winNum: Int? = null;

    fun parseRow(line: String) {
        this.add(Seq(line.trim().split(Regex(" +")).map { Cell(Integer.parseInt(it), false) }))
    }

    private fun fill() {
        for (i in 1 .. 5) {
            this.add(Seq(MutableList(5) { Cell(0, false) }))
        }
    }

    fun sendNum(num: Int) {
        if (!isFinished) {
            for (row in this) {
                row.sendNum(num)
            }
            if (isWinning()) {
                isFinished = true;
                winNum = num
            }
        }
    }

    fun setCounted() {
        isCounted = true
    }

    fun isWinning(): Boolean {
        return isRowsWinning() || trans().isRowsWinning()
    }

    private fun isRowsWinning(): Boolean {
        var isWinning = false;
        for (row in this) {
            if (row.isWinning()) {
                isWinning = true;
                break;
            }
        }
        return isWinning
    }

    fun trans(): Board {
        val transpose = Board(5)
        transpose.fill()
        for (i in 0 until size) {
            for (j in 0 until size) {
                transpose.set(j, i, this[i][j])
            }
        }
        return transpose;
    }

    private fun set(i: Int, j: Int, cell: Cell) {
        this[i][j] = cell;
    }

    fun sumUnmarked(): Int {
        var sum = 0;
        for(i in 0 until 5) {
            for(j in 0 until 5) {
                val cell = this[i][j]
                if (!cell.marked) {
                    sum += cell.intValue
                }
            }
        }
        return sum
    }
}

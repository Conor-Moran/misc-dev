package code.common

import java.util.*

enum class BinDigit (val char: Char) {
    ZERO('0'),
    ONE('1');

    fun inv(): BinDigit {
        return if (ZERO.char == this.char) ONE else ZERO;
    }
}

fun flipBits(n: Int, k: Int): Int {
    val mask = (1 shl k) - 1
    return n.inv() and mask
}

fun <E> MutableList<E>.getOrPut(index: Int, defaultValue: E): E {
    var elem = getOrNull(index)
    if (elem === null) {
        this.add(index, defaultValue)
    }
    return this[index]
}

fun String.isLowercase(): Boolean {
    return this.lowercase(Locale.getDefault()) == this
}

fun <E> perms(elems: List<E>): List<List<E>> {
    val ret = mutableListOf<MutableList<E>>();
    if (elems.size == 1) {
        ret.add(mutableListOf(elems[0]))
    } else {
        for (i in elems.indices) {
            val item = elems[i];
            val listOfNMinusOne = mutableListOf<E>();
            listOfNMinusOne.addAll(elems);
            listOfNMinusOne.remove(item);

            for (j in perms(listOfNMinusOne)) {
                val list = mutableListOf<E>()
                list.add(item)
                list.addAll(j)
                ret.add(list)
            }
        }
    }

    return ret
}

fun <T> twoDimIterate(data: Array<Array<T>>, fn: (T, Boolean, Int, Int) -> Unit) {
    for (i in data.indices) {
        var isNewLine = true
        for(j in data.indices) {
            fn(data[i][j], isNewLine, i, j)
            isNewLine = false
        }
    }
}

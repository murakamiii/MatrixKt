package model

import java.lang.Exception
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.pow
import kotlin.math.sqrt

interface MElement {
    fun value() : BigDecimal
    fun reciprocal() : MElement
    fun zero() : MElement
    fun one() : MElement
    operator fun times(other: Int) : MElement
    operator fun times(other: MElement) : MElement
    operator fun plus(other: MElement): MElement
    operator fun minus(other: MElement): MElement
}

data class Matrix(val comp: List<List<MElement>>) {
    companion object {
        fun make(comp: List<List<Int>>): Matrix {
            return when {
                comp.isEmpty() -> throw Exception("empty error")
                comp.first().isEmpty() -> throw Exception("empty error")
                !comp.all { it.size == comp.first().size } -> throw Exception("Matrix.make format error")
                else -> Matrix(comp.map { it.map { DecimalElement(it)}})
            }
        }
    }

    override fun toString(): String {
        return this.comp.foldIndexed("") { idx, acc, list ->
            acc + list.joinToString(prefix = " ", postfix = "\t") { it.toString() } +
                    "\n"
        }
    }
}

fun Matrix.rowNumber() = this.comp.size
fun Matrix.colNumber() = this.comp.first().size
fun Matrix.row(index: Int) = this.comp[index]
fun Matrix.col(index: Int) = this.comp.map { it[index] }

fun Matrix.makeMatrixSwappedRow(idx1: Int, idx2: Int) = Matrix(
    comp.mapIndexed { idx, list ->
        when(idx) {
            idx1 -> this.row(idx2)
            idx2 -> this.row(idx1)
            else -> list
        }
    }
)
fun Matrix.makeMatrixMultipleRow(idx: Int, times: Int) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == idx) { list.map { it * times }} else list
    }
)

fun Matrix.makeMatrixMultipleRow(idx: Int, times: MElement) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == idx) { list.map { times * it }} else list
    }
)

fun Matrix.makeMatrixAddMultipleRow(toIdx: Int, times: Int, fromIdx: Int) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == toIdx) { list.zip(row(fromIdx)) { to, from -> to + (from * times)}} else list
    }
)

fun Matrix.makeMatrixAddMultipleRow(toIdx: Int, times: MElement, fromIdx: Int) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == toIdx) { list.zip(row(fromIdx)) { to, from -> to + (times * from )}} else list
    }
)

fun Matrix.rowReducted(colNum: Int = colNumber()) : Matrix {
    var reducted = Matrix(comp)

    0.until(colNum).forEach { colIdx ->
        // 0でないならスワップする
        if (reducted.comp[colIdx][colIdx].value() == BigDecimal.ZERO) {
            val swapTarget = reducted.comp.drop(colIdx + 1).indexOfFirst { it[colIdx].value() != BigDecimal.ZERO}
            if (swapTarget == -1) { return@forEach }
            reducted = reducted.makeMatrixSwappedRow(colIdx, swapTarget + colIdx + 1)
        }

        // 逆数をかけて１にする
        reducted = reducted.makeMatrixMultipleRow(colIdx, reducted.comp[colIdx][colIdx].reciprocal())

        // 各行について0になるように調整
        0.until(rowNumber()).forEach {
            if (it != colIdx) {
                reducted = reducted.makeMatrixAddMultipleRow(it, reducted.comp[it][colIdx] * -1, colIdx)
            }
        }
    }

    return reducted
}

fun Matrix.zero() : MElement = this.comp[0][0].zero()
fun Matrix.one() : MElement = this.comp[0][0].one()
fun Matrix.identity(): Matrix {
    if (rowNumber() != colNumber()) {
        throw Exception("the identity needs same row & col length.")
    }
    val c = 0.until(rowNumber()).map { rowIdx -> 0.until(colNumber()).map { if (rowIdx == it) one() else zero() }}

    return Matrix(c)
}

fun Matrix.inverse() : Matrix {
    val reducted = Matrix(
        comp.zip(identity().comp) { left, right ->
            left.plus(right)
        }
    ).rowReducted(rowNumber())

    return Matrix(
        reducted.comp.map { it.drop(rowNumber()) }
    )
}

fun Matrix.isDiagonal() = comp.withIndex().all { indexedRow ->
    indexedRow.value.withIndex().all { indexedCol ->
        indexedRow.index == indexedCol.index || indexedCol.value.equals(0)
    }
}

fun Matrix.transposed() : Matrix {
    val newComp = 0.until(colNumber()).map { idx -> col(idx) }
    return Matrix(newComp)
}

fun Matrix.isSymmetric() = rowNumber() == colNumber() && 0.until(rowNumber()).all { rowIdx ->
    (rowIdx + 1).until(colNumber()).all { colIdx ->
        comp[rowIdx][colIdx] == comp[colIdx][rowIdx]
    }
}

fun Matrix.isSkewSymmetric() = rowNumber() == colNumber() && 0.until(rowNumber()).all { rowIdx ->
    (rowIdx + 1).until(colNumber()).all { colIdx ->
        comp[rowIdx][colIdx] == comp[colIdx][rowIdx] * -1
    }
}

fun Matrix.isUpperTriangular() = 0.until(rowNumber()).all { rowIdx ->
    (rowIdx - 1).downTo(0).all { colIdx ->
        comp[rowIdx][colIdx].equals(0)
    }
}

fun Matrix.output() {
    comp.forEach { row ->
        row.forEach{
            print("\t" + it)
        }
        println()
    }
}

fun Matrix.det() : MElement {
    if (rowNumber() != colNumber()) {
        throw Exception("the determinant needs same row & col length.")
    }
    return when(rowNumber()) {
        0, 1 -> throw Exception("the determinant needs the row & col length more than 1.")
        2 -> comp[0][0] * comp[1][1] - comp[0][1] * comp[1][0]
        else -> comp
            .mapIndexed { index, list ->
                (-1.0).pow(index).toInt() * list[0] * minor(index, 0).det()
            }
            .reduce { acc, ele -> acc + ele }
    }
}

fun Matrix.minor(row: Int, col: Int) = Matrix(
    comp.filterIndexed { index, list -> index != row }
        .map { it.filterIndexed { index, ele -> index != col } }
)

fun Matrix.eigenValue(): List<BigDecimal> {
    if (rowNumber() != colNumber()) {
        throw Exception("the eigenvalues need same row & col length.")
    }
    return when(rowNumber()) {
        0, 1 -> throw Exception("the eigenvalues need the row & col length more than 1.")
        2 -> solve2d(comp)
        else -> throw Exception("未実装")
    }
}

fun solve2d(comp: List<List<MElement>>): List<BigDecimal> {
    val aPlusD = (comp[0][0] + comp[1][1]).value()
    val aTimesD = (comp[0][0] * comp[1][1]).value()
    val bTimesC = (comp[0][1] * comp[1][0]).value()
    return listOf(
        0.5.toBigDecimal() * ( aPlusD + (aPlusD.pow(2) - 4.0.toBigDecimal() * (aTimesD - bTimesC)).sqrt(MathContext.DECIMAL64) ),
        0.5.toBigDecimal() * ( aPlusD - (aPlusD.pow(2) - 4.0.toBigDecimal() * (aTimesD - bTimesC)).sqrt(MathContext.DECIMAL64 ))
    )
}


/*
    演算子
 */

operator fun Matrix.plus(other: Matrix) : Matrix {
    if (this.rowNumber() != other.rowNumber() || this.colNumber() != other.colNumber()) {
        throw Exception("the sum needs same row & col length.")
    }
    return Matrix(
        comp.zip(other.comp) {it1, it2 -> it1.zip(it2) { el1, el2 ->
            el1 + el2
        }}
    )
}

operator fun Matrix.minus(other: Matrix) : Matrix {
    if (this.rowNumber() != other.rowNumber() || this.colNumber() != other.colNumber()) {
        throw Exception("the sum needs same row & col length.")
    }
    return Matrix(
        comp.zip(other.comp) {it1, it2 -> it1.zip(it2) { el1, el2 ->
            el1 - el2
        }}
    )
}

operator fun Matrix.times(times: Int) : Matrix = Matrix(
    comp.map { it.map { ele -> ele * times }}
)

operator fun Int.times(other: Matrix) = other * this
operator fun Int.times(other: MElement) = other * this

operator fun Matrix.times(other: Matrix) : Matrix {
    if (this.colNumber() != other.rowNumber()) {
        throw Exception("the product needs same lengths of lhs's col & rhs's row.")
    }

    return Matrix(
        comp.map { lhsRow -> 0.until(other.colNumber()).map { idx -> lhsRow.zip(other.col(idx)) { it1, it2 ->
                it1 * it2
            }.reduce { acc, mElement -> acc + mElement }
        }}
    )
}
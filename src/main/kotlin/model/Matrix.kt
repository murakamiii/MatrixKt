package model

import java.lang.Exception
import kotlin.math.pow

data class Matrix(val comp: List<List<MatrixElement>>) {
    companion object {
        fun make(comp: List<List<Int>>): Matrix {
            return when {
                comp.isEmpty() -> throw Exception("empty error")
                comp.first().isEmpty() -> throw Exception("empty error")
                !comp.all { it.size == comp.first().size } -> throw Exception("Matrix.make format error")
                else -> Matrix(comp.map { it.map { MatrixElement(it)}})
            }
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
        if (index == idx) { list.map { times * it }} else list
    }
)

fun Matrix.makeMatrixMultipleRow(idx: Int, times: MatrixElement) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == idx) { list.map { times * it }} else list
    }
)

fun Matrix.makeMatrixAddMultipleRow(toIdx: Int, times: Int, fromIdx: Int) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == toIdx) { list.zip(row(fromIdx)) { to, from -> to + (times * from )}} else list
    }
)

fun Matrix.makeMatrixAddMultipleRow(toIdx: Int, times: MatrixElement, fromIdx: Int) = Matrix(
    comp.mapIndexed { index, list ->
        if (index == toIdx) { list.zip(row(fromIdx)) { to, from -> to + (times * from )}} else list
    }
)

fun Matrix.rowReducted(colNum: Int = colNumber()) : Matrix {
    var reducted = Matrix(comp)

    0.until(colNum).forEach { colIdx ->
        // 0でないならスワップする
        if (reducted.comp[colIdx][colIdx].value() == 0.0) {
            val swapTarget = reducted.comp.drop(colIdx + 1).indexOfFirst { it[colIdx].value() != 0.0 }
            if (swapTarget == -1) { return@forEach }
            reducted = reducted.makeMatrixSwappedRow(colIdx, swapTarget + colIdx + 1)
        }

        // 逆数をかけて１にする
        reducted = reducted.makeMatrixMultipleRow(colIdx, reducted.comp[colIdx][colIdx].reciprocal())

        // 各行について0になるように調整
        0.until(rowNumber()).forEach {
            if (it != colIdx) {
                reducted = reducted.makeMatrixAddMultipleRow(it, -1 *  reducted.comp[it][colIdx], colIdx)
            }
        }
    }

    return reducted
}

fun Matrix.identity(): Matrix {
    if (rowNumber() != colNumber()) {
        throw Exception("the identity needs same row & col length.")
    }
    val c = 0.until(rowNumber()).map { rowIdx -> 0.until(colNumber()).map { if (rowIdx == it) 1 else 0 }}

    return Matrix.make(c)
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
        comp[rowIdx][colIdx] == -1 * comp[colIdx][rowIdx]
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

fun Matrix.det() : MatrixElement {
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
    comp.map { it.map { ele -> times * ele}}
)

operator fun Int.times(other: Matrix) = other * this

operator fun Matrix.times(other: Matrix) : Matrix {
    if (this.colNumber() != other.rowNumber()) {
        throw Exception("the product needs same lengths of lhs's col & rhs's row.")
    }

    return Matrix(
        comp.map { lhsRow -> 0.until(other.colNumber()).map { idx -> lhsRow.zip(other.col(idx)) { it1, it2 ->
                it1 * it2
            }.reduce { acc, matrixElement -> acc + matrixElement }
        }}
    )
}
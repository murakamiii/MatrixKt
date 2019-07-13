package model

import java.lang.Exception

data class Matrix(val comp: List<List<Int>>)

fun Matrix.rowNumber() = this.comp.size
fun Matrix.colNumber() = this.comp.first().size
fun Matrix.row(index: Int) = this.comp[index]
fun Matrix.col(index: Int) = this.comp.map { it[index] }

fun Matrix.isDiagonal() = comp.withIndex().all { indexedRow ->
    indexedRow.value.withIndex().all { indexedCol ->
        indexedRow.index == indexedCol.index || indexedCol.value == 0
    }
}

fun Matrix.transposed() : Matrix {
    val newComp = 0.until(colNumber()).map { idx -> col(idx) }
    return makeMatrix(newComp)!!
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
    rowIdx.downTo(0).all { colIdx ->
        println(comp[rowIdx][colIdx])
        comp[rowIdx][colIdx] == 0
    }
}

// TODO: あとでcompanion objectにする
fun makeMatrix(comp: List<List<Int>>) : Matrix? {
    return when {
        comp.isEmpty() -> null
        comp.first().isEmpty() -> null
        !comp.all { it.size == comp.first().size } -> null
        else -> Matrix(comp)
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

operator fun Matrix.plus(other: Matrix) : Matrix {
    if (this.rowNumber() != other.rowNumber() || this.colNumber() != other.colNumber()) {
        throw Exception("the sum needs same row & col length.")
    }
    return makeMatrix(
        comp.zip(other.comp) {it1, it2 -> it1.zip(it2) { el1, el2 ->
            el1 + el2
        }}
    )!!
}

operator fun Matrix.minus(other: Matrix) : Matrix {
    if (this.rowNumber() != other.rowNumber() || this.colNumber() != other.colNumber()) {
        throw Exception("the sum needs same row & col length.")
    }
    return makeMatrix(
        comp.zip(other.comp) {it1, it2 -> it1.zip(it2) { el1, el2 ->
            el1 - el2
        }}
    )!!
}

operator fun Matrix.times(times: Int) : Matrix {
    return makeMatrix(
        comp.map { it.map { ele -> ele * times}}
    )!!
}

operator fun Int.times(other: Matrix) = other * this

operator fun Matrix.times(other: Matrix) : Matrix {
    if (this.colNumber() != other.rowNumber()) {
        throw Exception("the product needs same lengths of lhs's col & rhs's row.")
    }

    return makeMatrix(
        comp.map { lhsRow -> 0.until(other.colNumber()).map { idx -> lhsRow.zip(other.col(idx)) { it1, it2 ->
                it1 * it2
            }.sum()
        }}
    )!!
}
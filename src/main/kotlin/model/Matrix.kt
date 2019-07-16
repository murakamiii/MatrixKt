package model

import java.lang.Exception

data class Matrix(val comp: List<List<MatrixElement>>) {
    companion object {
        fun make(comp: List<List<Int>>): Matrix {
            return when {
                comp.isEmpty() -> throw Exception("empty error")
                comp.first().isEmpty() -> throw Exception("empty error")
                !comp.all { it.size == comp.first().size } -> throw Exception("empty error")
                else -> Matrix(comp.map { it.map { MatrixElement(it)}})
            }
        }
    }
}

fun Matrix.rowNumber() = this.comp.size
fun Matrix.colNumber() = this.comp.first().size
fun Matrix.row(index: Int) = this.comp[index]
fun Matrix.col(index: Int) = this.comp.map { it[index] }

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
package model

import java.lang.Exception

data class Matrix(val comp: List<List<Int>>)

fun Matrix.rowNumber() = this.comp.size
fun Matrix.colNumber() = this.comp.first().size

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
package model

data class Matrix<Number>(val comp: List<List<Number>>)

fun Matrix<Number>.rowNumber() = this.comp.size
fun Matrix<Number>.colNumber() = this.comp.first().size

fun makeMatrix(comp: List<List<Number>>) : Matrix<Number>? {
    return when {
        comp.isEmpty() -> null
        comp.first().isEmpty() -> null
        !comp.all { it.size == comp.first().size } -> null
        else -> Matrix(comp)
    }
}
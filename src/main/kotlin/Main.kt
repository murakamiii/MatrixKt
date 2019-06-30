import model.colNumber
import model.makeMatrix
import model.rowNumber

fun main() {
    println("Hello, World!")
    val mat = makeMatrix(
        listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6)
        )
    )
    mat?.let {
        println(it.rowNumber())
        println(it.colNumber())
    } ?: run {
        println("nullです")
    }

    val nullMat = makeMatrix(
        listOf(
            listOf(1, 2, 3),
            listOf(4, 5)
        )
    )
    nullMat?.let {
        println(it.rowNumber())
        println(it.colNumber())
    } ?: run {
        println("nullです")
    }
}
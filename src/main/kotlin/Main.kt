import model.*
import java.lang.Exception

fun main() {


    // https://qiita.com/NoriakiOshita/items/460247bb57c22973a5f0
    val testlist = listOf(
        mapOf("Python" to 70.0, "Java" to 30.0, "fee" to 700.0),
        mapOf("Python" to 32.0, "Java" to 60.0, "fee" to 480.0),
        mapOf("Python" to 32.0, "Java" to 20.0, "fee" to 300.0),
        mapOf("Python" to 20.0, "Java" to 120.0, "fee" to 600.0),
        mapOf("Python" to 40.0, "Java" to 120.0, "fee" to 630.0),
        mapOf("Python" to 40.0, "Java" to 30.0, "fee" to 520.0),
        mapOf("Python" to 300.0, "Java" to 1100.0, "fee" to 1200.0),
        mapOf("Python" to 2000.0, "Java" to 400.0, "fee" to 1500.0),
        mapOf("Python" to 40.0, "Java" to 180.0, "fee" to 800.0)
    )
    val hmat = makeCorrMatrix(testlist)
    println(hmat)
    println(hmat.header)
    println(hmat.value)

    val matt = Matrix(
        listOf(
            listOf(
                DoubleElement(1.0),
                DoubleElement(3.0),
                DoubleElement(3.0),
                DoubleElement(1.0)
            ),
            listOf(
                DoubleElement(3.0),
                DoubleElement(2.0),
                DoubleElement(0.0),
                DoubleElement(2.0)
            ),
            listOf(
                DoubleElement(3.0),
                DoubleElement(0.0),
                DoubleElement(3.0),
                DoubleElement(1.0)
            ),
            listOf(
                DoubleElement(1.0),
                DoubleElement(2.0),
                DoubleElement(1.0),
                DoubleElement(1.0)
            )
        )
    )
    val res = jacobi(matt)

    println("ループ数: ${res.first}")
    println("結果:\n ${res.second}")
}
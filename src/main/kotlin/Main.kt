import model.*
import java.lang.Exception

fun main() {
    try {
        val nullMat = Matrix.make(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5)
            )
        )
    } catch (e: Exception) {
        println(e)
    }


    val mat = Matrix.make(
        listOf(
            listOf(1, 2, 33),
            listOf(40, 5, 6)
        )
    )

    mat.let {
        println(it.rowNumber())
        println(it.colNumber())
        println(it.col(1))
    }

    val mat2 = Matrix.make(
        listOf(
            listOf(1, 20, 3),
            listOf(22, -52, 0)
        )
    )

    println("\n足し算")
    (mat + mat2).output()
    println("\n引き算")
    (mat - mat2).output()
    println("\n掛け算")
    (mat * 2).output()
    println("\n掛け算")
    (3 * mat).output()

    val lhs = Matrix.make(
        listOf(
            listOf(2, 3),
            listOf(1, 4),
            listOf(-1, 0),
            listOf(1, 5)
        )
    )

    val rhs = Matrix.make(
        listOf(
            listOf(2, 0, -3),
            listOf(-1, 5, 4)
        )
    )

    println("\n行列積")
    (lhs * rhs).output()
    println()

    val nonDiagonal = Matrix.make(
        listOf(
            listOf(2, 0, -3),
            listOf(0, 5, 0),
            listOf(0, 0, 4)
        )
    )
    println("nonDiagonal.isDiagonal() : " + nonDiagonal.isDiagonal())

    val diagonal = Matrix.make(
        listOf(
            listOf(2, 0, 0),
            listOf(0, 5, 0),
            listOf(0, 0, 4)
        )
    )
    println("diagonal.isDiagonal() : " + diagonal.isDiagonal())
    println()

    println("転置行列")
    rhs.transposed().output()

    val symmetric = Matrix.make(
        listOf(
            listOf(1, -5, 4),
            listOf(-5, 0, 3),
            listOf(4, 3, 2)
        )
    ).isSymmetric()
    println("対称行列？: " + symmetric)

    val nonSymmetric = Matrix.make(
        listOf(
            listOf(1, -5, 4),
            listOf(-5, 0, 3),
            listOf(4, -3, 2)
        )
    ).isSymmetric()
    println("対称行列？: " + nonSymmetric)

    val skewSymmetric = Matrix.make(
        listOf(
            listOf(1, -5, 4),
            listOf(5, 0, 3),
            listOf(-4, -3, 2)
        )
    ).isSkewSymmetric()
    println("交代行列？: " + skewSymmetric)

    val nonSkewSymmetric = Matrix.make(
        listOf(
            listOf(1, -5, 4),
            listOf(5, 0, 3),
            listOf(-4, 3, 2)
        )
    ).isSkewSymmetric()
    println("交代行列？: " + nonSkewSymmetric)

    val upperTriangular = Matrix.make(
        listOf(
            listOf(1, 2, 5),
            listOf(0, -1, 0),
            listOf(0, 0, -3)
        )
    ).isUpperTriangular()
    println("上三角行列？: " + upperTriangular)

    val nonUpperTrianglar = Matrix.make(
        listOf(
            listOf(1, 2, 5),
            listOf(0, -1, 0),
            listOf(0, 1, -3)
        )
    ).isUpperTriangular()
    println("上三角行列？: " + nonUpperTrianglar)

    Matrix.make(
        listOf(
            listOf(1, 20, 3),
            listOf(2, 21, 4),
            listOf(3, 22, 5),
            listOf(22, -52, 0)
        )
    ).makeMatrixSwappedRow(0, 3).output()
    println()
    Matrix.make(
        listOf(
            listOf(1, 20, 3),
            listOf(2, 21, 4),
            listOf(3, 22, 5),
            listOf(22, -52, 0)
        )
    ).makeMatrixMultipleRow(3, -10).output()

}
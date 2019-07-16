import model.*

fun main() {
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

    val mat = makeMatrix(
        listOf(
            listOf(1, 2, 33),
            listOf(40, 5, 6)
        )
    )

    mat?.let {
        println(it.rowNumber())
        println(it.colNumber())
        println(it.col(1))
    }

    val mat2 = makeMatrix(
        listOf(
            listOf(1, 20, 3),
            listOf(22, -52, 0)
        )
    )!!

    println("\n足し算")
    (mat!! + mat2).output()
    println("\n引き算")
    (mat!! - mat2).output()
    println("\n掛け算")
    (mat!! * 2).output()
    println("\n掛け算")
    (3 * mat!!).output()

    val lhs = makeMatrix(
        listOf(
            listOf(2, 3),
            listOf(1, 4),
            listOf(-1, 0),
            listOf(1, 5)
        )
    )!!

    val rhs = makeMatrix(
        listOf(
            listOf(2, 0, -3),
            listOf(-1, 5, 4)
        )
    )!!

    println("\n行列積")
    (lhs * rhs).output()
    println()

    val nonDiagonal = makeMatrix(
        listOf(
            listOf(2, 0, -3),
            listOf(0, 5, 0),
            listOf(0, 0, 4)
        )
    )!!
    println("nonDiagonal.isDiagonal() : " + nonDiagonal.isDiagonal())

    val diagonal = makeMatrix(
        listOf(
            listOf(2, 0, 0),
            listOf(0, 5, 0),
            listOf(0, 0, 4)
        )
    )!!
    println("diagonal.isDiagonal() : " + diagonal.isDiagonal())
    println()

    println("転置行列")
    rhs.transposed().output()

    val symmetric = makeMatrix(
        listOf(
            listOf(1, -5, 4),
            listOf(-5, 0, 3),
            listOf(4, 3, 2)
        )
    )!!.isSymmetric()
    println("対称行列？: " + symmetric)

    val nonSymmetric = makeMatrix(
        listOf(
            listOf(1, -5, 4),
            listOf(-5, 0, 3),
            listOf(4, -3, 2)
        )
    )!!.isSymmetric()
    println("対称行列？: " + nonSymmetric)

    val skewSymmetric = makeMatrix(
        listOf(
            listOf(1, -5, 4),
            listOf(5, 0, 3),
            listOf(-4, -3, 2)
        )
    )!!.isSkewSymmetric()
    println("交代行列？: " + skewSymmetric)

    val nonSkewSymmetric = makeMatrix(
        listOf(
            listOf(1, -5, 4),
            listOf(5, 0, 3),
            listOf(-4, 3, 2)
        )
    )!!.isSkewSymmetric()
    println("交代行列？: " + nonSkewSymmetric)

    val upperTriangular = makeMatrix(
        listOf(
            listOf(1, 2, 5),
            listOf(0, -1, 0),
            listOf(0, 0, -3)
        )
    )!!.isUpperTriangular()
    println("上三角行列？: " + upperTriangular)

    val nonUpperTrianglar = makeMatrix(
        listOf(
            listOf(1, 2, 5),
            listOf(0, -1, 0),
            listOf(0, 1, -3)
        )
    )!!.isUpperTriangular()
    println("上三角行列？: " + nonUpperTrianglar)

    val me = MatrixElement(20, 4)
    val me2 = MatrixElement(1, 3)
    val me3 = MatrixElement(1, 2)

}
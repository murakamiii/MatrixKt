package model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception

internal class MatrixKtTest {

    @Test
    fun rowNumber() {
        val m1 = Matrix.make(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )
        assertTrue(m1.rowNumber() == 2)
    }

    @Test
    fun colNumber() {
        val m1 = Matrix.make(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )
        assertEquals(3, m1.colNumber())
    }

    @Test
    fun getComp() {
        assertThrows<Exception> {
            val m1 = Matrix.make(
                listOf(
                    listOf(1, 2, 3),
                    listOf(4, 5)
                )
            )
        }

        assertThrows<Exception> {
            val m2 = Matrix.make(
                listOf(
                    emptyList()
                )
            )
        }


        assertDoesNotThrow {
            val m3 = Matrix.make(
                listOf(
                    listOf(1, 2, 3),
                    listOf(4, 5, 6)
                )
            )
            println(m3)
        }
    }

    @Test
    fun plus() {
        val m1 = Matrix.make(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )
        val m2 = Matrix.make(
            listOf(
                listOf(1, 2, 3)
            )
        )
        assertThrows(Exception::class.java ) { m1 + m2 }

        val m3 = Matrix.make(
            listOf(
                listOf(1, 2),
                listOf(3, 4)
            )
        )
        assertThrows(Exception::class.java ) { m1 + m3 }

        val res = Matrix.make(
            listOf(
                listOf(2, 4, 6),
                listOf(8, 10, 12)
            )
        )
        assertEquals( res,m1 + m1)
    }

    @Test
    fun minus() {
        val m1 = Matrix.make(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )
        val m2 = Matrix.make(
            listOf(
                listOf(1, 2, 3)
            )
        )
        assertThrows(Exception::class.java ) { m1 - m2 }

        val m3 = Matrix.make(
            listOf(
                listOf(1, 2),
                listOf(3, 4)
            )
        )
        assertThrows(Exception::class.java ) { m1 - m3 }

        val res = Matrix.make(
            listOf(
                listOf(0, 0, 0),
                listOf(0, 0, 0)
            )
        )
        assertEquals( res,m1 - m1)

    }

    @Test
    fun times() {
        val m1 = Matrix.make(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )

        val res = Matrix.make(
            listOf(
                listOf(0, 0, 0),
                listOf(0, 0, 0)
            )
        )
        assertEquals( res,m1 * 0)
        assertEquals( res,0 * m1)

        val res2 = Matrix.make(
            listOf(
                listOf(-2, -4, -6),
                listOf(-8, -10, -12)
            )
        )
        assertEquals( res2,m1 * -2)
        assertEquals( res2,-2 * m1)

        assertThrows(Exception::class.java ) { m1 * res2 }

        val m2 = Matrix.make(
            listOf(
                listOf(0),
                listOf(0),
                listOf(0)
            )
        )
        val res3 = Matrix.make(
            listOf(
                listOf(0),
                listOf(0)
            )
        )
        assertEquals(res3, m1 * m2)
    }

    @Test
    fun isDiagonal() {
        val nonDiagonal = Matrix.make(
            listOf(
                listOf(2, 0, -3),
                listOf(0, 5, 0),
                listOf(0, 0, 4)
            )
        )
        assertFalse(nonDiagonal.isDiagonal())

        val diagonal = Matrix.make(
            listOf(
                listOf(2, 0, 0),
                listOf(0, 5, 0),
                listOf(0, 0, 4)
            )
        )
        assertTrue(diagonal.isDiagonal())
    }

    @Test
    fun transposed() {
        val mat = Matrix.make(
            listOf(
                listOf(-2, 3, 1),
                listOf(6, 0, 9)
            )
        )

        val expect = Matrix.make(
            listOf(
                listOf(-2, 6),
                listOf(3, 0),
                listOf(1, 9)
            )
        )

        assertEquals(mat.transposed(), expect)
    }

    @Test
    fun isSymmetric() {
        val mat = Matrix.make(
            listOf(
                listOf(2, -3, 5),
                listOf(-3, 1, -4)
            )
        )
        assertFalse(mat.isSymmetric())

        val mat2 = Matrix.make(
            listOf(
                listOf(2, -3, 5),
                listOf(-3, 1, -4),
                listOf(5, 4, 3)
            )
        )
        assertFalse(mat2.isSymmetric())

        val mat3 = Matrix.make(
            listOf(
                listOf(2, -3, 5),
                listOf(-3, 1, -4),
                listOf(5, -4, 3)
            )
        )
        assertTrue(mat3.isSymmetric())
    }

    @Test
    fun isSkewSymmetric() {
        val mat = Matrix.make(
            listOf(
                listOf(0, -5, 4, 1),
                listOf(5, 0, -3, 2),
                listOf(-4, 3, 0, 6),
                listOf(-1, -2, -6, 0)
            )
        )
        assertTrue(mat.isSkewSymmetric())
        val mat2 = Matrix.make(
            listOf(
                listOf(0, -5, 4, 1),
                listOf(5, 0, -3, 2),
                listOf(-4, 3, 0, 6),
                listOf(1, -2, -6, 0)
            )
        )
        assertFalse(mat2.isSkewSymmetric())
    }

    @Test
    fun isUpperTriangular() {
        val mat = Matrix.make(
            listOf(
                listOf(1, -5, 4),
                listOf(0, 4, -3),
                listOf(0, 0, 7)
            )
        )
        assertTrue(mat.isUpperTriangular())

        val mat2 = Matrix.make(
            listOf(
                listOf(1, -5, 4),
                listOf(0, 4, -3),
                listOf(0, 1, 7)
            )
        )
        assertFalse(mat2.isUpperTriangular())
    }
}
package model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class MatrixKtTest {

    @org.junit.jupiter.api.Test
    fun rowNumber() {
        val m1 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )!!
        assertTrue(m1.rowNumber() == 2)
    }

    @org.junit.jupiter.api.Test
    fun colNumber() {
        val m1 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )!!
        assertEquals(3, m1.colNumber())
    }

    @org.junit.jupiter.api.Test
    fun makeMatrix() {
        val m1 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5)
            )
        )
        assertTrue(m1 == null)

        val m2 = makeMatrix(
            listOf(
                emptyList()
            )
        )
        assertTrue(m2 == null)

        val m3 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )
        assertTrue(m3 != null)
    }

    @org.junit.jupiter.api.Test
    fun plus() {
        val m1 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )!!
        val m2 = makeMatrix(
            listOf(
                listOf(1, 2, 3)
            )
        )!!
        assertThrows(Exception::class.java ) { m1 + m2 }

        val m3 = makeMatrix(
            listOf(
                listOf(1, 2),
                listOf(3, 4)
            )
        )!!
        assertThrows(Exception::class.java ) { m1 + m3 }

        val res = makeMatrix(
            listOf(
                listOf(2, 4, 6),
                listOf(8, 10, 12)
            )
        )!!
        assertEquals( res,m1 + m1)
    }

    @org.junit.jupiter.api.Test
    fun minus() {
        val m1 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )!!
        val m2 = makeMatrix(
            listOf(
                listOf(1, 2, 3)
            )
        )!!
        assertThrows(Exception::class.java ) { m1 - m2 }

        val m3 = makeMatrix(
            listOf(
                listOf(1, 2),
                listOf(3, 4)
            )
        )!!
        assertThrows(Exception::class.java ) { m1 - m3 }

        val res = makeMatrix(
            listOf(
                listOf(0, 0, 0),
                listOf(0, 0, 0)
            )
        )!!
        assertEquals( res,m1 - m1)

    }

    @org.junit.jupiter.api.Test
    fun times() {
        val m1 = makeMatrix(
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6)
            )
        )!!

        val res = makeMatrix(
            listOf(
                listOf(0, 0, 0),
                listOf(0, 0, 0)
            )
        )!!
        assertEquals( res,m1 * 0)
        assertEquals( res,0 * m1)

        val res2 = makeMatrix(
            listOf(
                listOf(-2, -4, -6),
                listOf(-8, -10, -12)
            )
        )!!
        assertEquals( res2,m1 * -2)
        assertEquals( res2,-2 * m1)

        assertThrows(Exception::class.java ) { m1 * res2 }

        val m2 = makeMatrix(
            listOf(
                listOf(0),
                listOf(0),
                listOf(0)
            )
        )!!
        val res3 = makeMatrix(
            listOf(
                listOf(0),
                listOf(0)
            )
        )
        assertEquals(res3, m1 * m2)
    }

    @org.junit.jupiter.api.Test
    fun isDiagonal() {
        val nonDiagonal = makeMatrix(
            listOf(
                listOf(2, 0, -3),
                listOf(0, 5, 0),
                listOf(0, 0, 4)
            )
        )!!
        assertFalse(nonDiagonal.isDiagonal())

        val diagonal = makeMatrix(
            listOf(
                listOf(2, 0, 0),
                listOf(0, 5, 0),
                listOf(0, 0, 4)
            )
        )!!
        assertTrue(diagonal.isDiagonal())
    }

    @org.junit.jupiter.api.Test
    fun transposed() {
        val mat = makeMatrix(
            listOf(
                listOf(-2, 3, 1),
                listOf(6, 0, 9)
            )
        )!!

        val expect = makeMatrix(
            listOf(
                listOf(-2, 6),
                listOf(3, 0),
                listOf(1, 9)
            )
        )!!

        assertEquals(mat.transposed(), expect)
    }

    @org.junit.jupiter.api.Test
    fun isSymmetric() {
        val mat = makeMatrix(
            listOf(
                listOf(2, -3, 5),
                listOf(-3, 1, -4)
            )
        )!!
        assertFalse(mat.isSymmetric())

        val mat2 = makeMatrix(
            listOf(
                listOf(2, -3, 5),
                listOf(-3, 1, -4),
                listOf(5, 4, 3)
            )
        )!!
        assertFalse(mat2.isSymmetric())

        val mat3 = makeMatrix(
            listOf(
                listOf(2, -3, 5),
                listOf(-3, 1, -4),
                listOf(5, -4, 3)
            )
        )!!
        assertTrue(mat3.isSymmetric())
    }

    @org.junit.jupiter.api.Test
    fun isSkewSymmetric() {
        val mat = makeMatrix(
            listOf(
                listOf(0, -5, 4, 1),
                listOf(5, 0, -3, 2),
                listOf(-4, 3, 0, 6),
                listOf(-1, -2, -6, 0)
            )
        )!!
        assertTrue(mat.isSkewSymmetric())
        val mat2 = makeMatrix(
            listOf(
                listOf(0, -5, 4, 1),
                listOf(5, 0, -3, 2),
                listOf(-4, 3, 0, 6),
                listOf(1, -2, -6, 0)
            )
        )!!
        assertFalse(mat2.isSkewSymmetric())
    }

    @Test
    fun isUpperTriangular() {
        val mat = makeMatrix(
            listOf(
                listOf(1, -5, 4),
                listOf(0, 4, -3),
                listOf(0, 0, 7)
            )
        )!!
        assertTrue(mat.isUpperTriangular())

        val mat2 = makeMatrix(
            listOf(
                listOf(1, -5, 4),
                listOf(0, 4, -3),
                listOf(0, 1, 7)
            )
        )!!
        assertFalse(mat.isUpperTriangular())
    }
}
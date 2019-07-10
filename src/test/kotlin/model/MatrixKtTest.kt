package model

import org.junit.jupiter.api.Assertions.*
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
        assertEquals( res,m1 * -2)
        assertEquals( res,-2 * m1)
    }
}
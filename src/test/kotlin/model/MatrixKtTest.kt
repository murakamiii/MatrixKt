package model

import org.junit.jupiter.api.Assertions.*

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
        assertEquals(33, m1.colNumber())
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
}
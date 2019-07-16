package model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MatrixElementKtTest {

    @Test
    fun value() {
        assertEquals(1.0, MatrixElement(1).value())
        assertEquals(-10.0, MatrixElement(-10).value())
        assertEquals(0.0, MatrixElement(0, 1).value())
        // assertEquals では -0.0 != 0.0
        assertTrue(0.0 == MatrixElement(0, -1).value())
        assertEquals(1.0, MatrixElement(1, 1).value())
        assertEquals(-1.0, MatrixElement(1, -1).value())
        assertEquals(-1.0, MatrixElement(-1, 1).value())
        assertEquals(1.0, MatrixElement(-1, -1).value())
        assertEquals(4.0, MatrixElement(100, 25).value())
        assertEquals(1.0/3.0, MatrixElement(1, 3).value())
        assertEquals(0.25, MatrixElement(1, 4).value())
    }

    @Test
    fun equals1() {
        assertTrue(MatrixElement(0) == MatrixElement(0))
        assertTrue(MatrixElement(1, 2) == MatrixElement(2, 4))
    }

    @Test
    fun plus() {
        assertEquals(MatrixElement(2), MatrixElement(1) + MatrixElement(1))
        assertEquals(MatrixElement(5,2), MatrixElement(1) + MatrixElement(3, 2))
        assertEquals(MatrixElement(1), MatrixElement(1, 3) + MatrixElement(1, 3) + MatrixElement(1, 3))
        assertEquals(MatrixElement(5,6), MatrixElement(1, 2) + MatrixElement(1, 3))
        assertEquals(MatrixElement(2), MatrixElement(1, 4) + MatrixElement(7, 4))
    }

    @Test
    fun minus() {
        assertEquals(MatrixElement(0), MatrixElement(1) - MatrixElement(1))
        assertEquals(MatrixElement(2), MatrixElement(3) - MatrixElement(1))
        assertEquals(MatrixElement(0), MatrixElement(1, 3) - MatrixElement(1, 3))
        assertEquals(MatrixElement(-1, 24), MatrixElement(1, 8) - MatrixElement(1, 6))
        assertEquals(MatrixElement(5, 3), MatrixElement(2) - MatrixElement(1, 3))
    }

    @Test
    fun times() {
        assertEquals(MatrixElement(0), MatrixElement(0) * MatrixElement(0))
        assertEquals(MatrixElement(0), MatrixElement(100) * MatrixElement(0))
        assertEquals(MatrixElement(0), MatrixElement(-100) * MatrixElement(0))
        assertEquals(MatrixElement(60), MatrixElement(30) * MatrixElement(2))
        assertEquals(MatrixElement(3, 2), MatrixElement(3, 4) * MatrixElement(2))
        assertEquals(MatrixElement(7, 15), MatrixElement(1, 3) * MatrixElement(7, 5))
    }
}
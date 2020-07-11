package model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal

internal class DecimalElementTest {

    @Test
    fun value() {
        assertEquals(1.toBigDecimal(), DecimalElement(1).value())
        assertTrue((-10.0).toBigDecimal().compareTo(DecimalElement(-10).value()) == 0)
        assertTrue(0.0.toBigDecimal().compareTo(DecimalElement(0).value()) == 0)
        assertEquals(0.123.toBigDecimal(), DecimalElement(0.123.toBigDecimal()).value())
    }

    @Test
    fun reciprocal() {
        assertEquals(DecimalElement(1), DecimalElement(1).reciprocal())
        assertEquals(DecimalElement(0.1.toBigDecimal()), DecimalElement(10).reciprocal())
        assertEquals(DecimalElement((-2).toBigDecimal()), DecimalElement((-0.5).toBigDecimal()).reciprocal())
    }

    @Test
    fun zero() {
        assertEquals(BigDecimal.ZERO, DecimalElement(1).zero().value())
    }

    @Test
    fun one() {
        assertEquals(BigDecimal.ONE, DecimalElement(0).one().value())
    }

    @Test
    fun times() {
        assertEquals(DecimalElement(0), DecimalElement(0) * 1)
        assertEquals(DecimalElement(1), DecimalElement(1) * 1)
        assertEquals(DecimalElement(20), DecimalElement(-4) * -5)
        assertEquals(DecimalElement((-6.9).toBigDecimal()), DecimalElement(2.3.toBigDecimal()) * -3)

        assertEquals(DecimalElement(0), DecimalElement(2.3.toBigDecimal()) * DecimalElement(0))
        assertEquals(DecimalElement(8), DecimalElement(-2) * DecimalElement(-4))
        assertEquals(DecimalElement(-4), DecimalElement(1) * DecimalElement(-4))
        assertEquals(DecimalElement(-20), DecimalElement(40) * DecimalElement((-0.5).toBigDecimal()))
    }

    @Test
    fun plus() {
        assertEquals(DecimalElement(0), DecimalElement(0) + DecimalElement(0))
        assertEquals(DecimalElement(2), DecimalElement(1) + DecimalElement(1))
        assertEquals(DecimalElement(4.3.toBigDecimal()), DecimalElement(-10) + DecimalElement(14.3.toBigDecimal()))
    }

    @Test
    fun minus() {
        assertEquals(DecimalElement(0), DecimalElement(0) - DecimalElement(0))
        assertEquals(DecimalElement(0), DecimalElement(1) - DecimalElement(1))
        assertEquals(DecimalElement((-24.3).toBigDecimal()), DecimalElement(-10) - DecimalElement(14.3.toBigDecimal()))
    }
}
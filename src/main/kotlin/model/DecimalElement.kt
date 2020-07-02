package model

import java.math.BigDecimal

data class DecimalElement(val element: BigDecimal) : MElement {
    override fun value(): Double = this.element.toDouble()

    override fun reciprocal(): MElement = DecimalElement(BigDecimal.ONE / this.element)

    override fun zero(): MElement = DecimalElement(BigDecimal.ZERO)

    override fun one(): MElement = DecimalElement(BigDecimal.ONE)

    override fun times(other: Int): MElement =
        DecimalElement(this.element * BigDecimal.valueOf(other.toLong()))

    override fun times(other: MElement): MElement =
        DecimalElement(this.element * BigDecimal.valueOf(other.value()))

    override fun plus(other: MElement): MElement =
        DecimalElement(this.element + BigDecimal.valueOf(other.value()))

    override fun minus(other: MElement): MElement =
        DecimalElement(this.element + BigDecimal.valueOf(other.value()))
}
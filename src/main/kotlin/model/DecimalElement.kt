package model

import java.math.BigDecimal

data class DecimalElement(val element: BigDecimal) : MElement {
    constructor(number: Int) : this(number.toBigDecimal())

    override fun value(): BigDecimal = this.element

    override fun reciprocal(): MElement = DecimalElement(BigDecimal.ONE / this.element)

    override fun zero(): MElement = DecimalElement(BigDecimal.ZERO)

    override fun one(): MElement = DecimalElement(BigDecimal.ONE)

    override fun times(other: Int): MElement =
        DecimalElement(this.element * other.toBigDecimal())

    override fun times(other: MElement): MElement =
        DecimalElement(this.element * other.value())

    override fun plus(other: MElement): MElement =
        DecimalElement(this.element + other.value())

    override fun minus(other: MElement): MElement =
        DecimalElement(this.element + other.value())

    override fun toString(): String {
        return String.format("%.7f", element)
    }
}
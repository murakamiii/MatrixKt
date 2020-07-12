package model

import java.math.BigDecimal
import java.math.RoundingMode

data class DecimalElement(val element: BigDecimal) : MElement {
    constructor(number: Int) : this(number.toBigDecimal())

    override fun value(): BigDecimal = this.element

    override fun reciprocal(): MElement = DecimalElement(BigDecimal.ONE / this.element)

    override fun zero(): MElement = DecimalElement(BigDecimal.ZERO)
    override fun isZero(): Boolean = this == this.zero()

    override fun one(): MElement = DecimalElement(BigDecimal.ONE)

    override fun times(other: Int): MElement =
        DecimalElement(this.element * other.toBigDecimal())

    override fun times(other: MElement): MElement =
        DecimalElement(this.element * other.value())

    override fun plus(other: MElement): MElement =
        DecimalElement(this.element + other.value())

    override fun minus(other: MElement): MElement =
        DecimalElement(this.element - other.value())

    override fun toString(): String {
        return String.format("%.7f", element)
    }

    override fun equals(other: Any?): Boolean {
        if (other is DecimalElement) {
            return this.element.setScale(7, RoundingMode.HALF_UP).compareTo(other.value().setScale(7, RoundingMode.HALF_UP)) == 0
        }
        return super.equals(other)
    }
}

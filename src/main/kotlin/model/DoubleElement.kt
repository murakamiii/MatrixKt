package model

import java.lang.Error

data class DoubleElement(val rawValue: Double): MElement {
    override fun value() = rawValue
    override fun reciprocal() = DoubleElement(1.0 / rawValue)

    override fun plus(other: MElement) = if (other is DoubleElement) DoubleElement(
        this.rawValue + other.rawValue
    ) else throw Error("同じ型で計算して")

    override fun minus(other: MElement) = if (other is DoubleElement) DoubleElement(
        this.rawValue - other.rawValue
    ) else throw Error("同じ型で計算して")

    override fun times(other: Int) = DoubleElement(this.rawValue * other.toDouble())
    override fun times(other: MElement) = when (other) {
        is MatrixElement -> DoubleElement(
            this.value() * other.value()
        )
        is DoubleElement -> DoubleElement(
            this.value() * other.value()
        )
        else -> throw Error("同じ型で計算して")
    }

    override fun toString(): String {
        return String.format("%.7f", rawValue)
    }

    override fun zero() = DoubleElement(0.0)
    override fun one() = DoubleElement(1.0)
}
package model

data class MatrixElement(val numerator: Int, val denominator: NonZeroInt) {
    constructor(value: Int) : this(value, 1.toNonZeroInt()){}
    constructor(numerator: Int, denominator: Int) : this(numerator, denominator.toNonZeroInt()){}

    fun value(): Double {
        if (numerator == 0) return 0.0
        return numerator.toDouble()/denominator.toDouble()
    }

    private fun fmt(d: Double): String {
        return d.toString().replace(".0", "")
    }

    override fun equals(other: Any?): Boolean = when {
        other is Int -> value() == other.toDouble()
        other is MatrixElement -> this.value() == other.value()
        else -> super.equals(other)
    }

    override fun toString(): String {
        return fmt(value())
    }

    fun reciprocal() = if (numerator == 0) MatrixElement(0) else MatrixElement(denominator.toInt(), numerator)
}

operator fun MatrixElement.plus(other: MatrixElement): MatrixElement = MatrixElement(
    numerator * other.denominator.toInt() + other.numerator * denominator.toInt(),
    denominator.toInt() * other.denominator.toInt()
)

operator fun MatrixElement.minus(other: MatrixElement): MatrixElement = MatrixElement(
    numerator * other.denominator.toInt() - other.numerator * denominator.toInt(),
    denominator.toInt() * other.denominator.toInt()
)

operator fun Int.times(other: MatrixElement) = MatrixElement(other.numerator * this, other.denominator)

operator fun MatrixElement.times(other: MatrixElement) = MatrixElement(
    numerator * other.numerator,
    denominator * other.denominator
)

/*
    分母に使うやつ
 */
data class NonZeroInt(private val rawValue: Int) {
    init {
        if (rawValue == 0) {
            throw Exception("NonZeroInt.rawValue should be non zero")
        }
    }

    fun toDouble() = rawValue.toDouble()
    fun toInt() = rawValue
}

fun Int.toNonZeroInt() = NonZeroInt(this)
operator fun NonZeroInt.times(other: NonZeroInt) = NonZeroInt(this.toInt() * other.toInt())
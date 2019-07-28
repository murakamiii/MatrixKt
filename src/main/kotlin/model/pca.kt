package model

import kotlin.math.*


data class MatrixWithHeader(val header: List<String>, val value: Matrix) {
    override fun toString(): String {
        val headerStr =
            "          " +
            header.joinToString(postfix = "\t") { String.format("%9s", it) } +
            "\n"
        val matStr = value.comp.foldIndexed("") { idx, acc, list ->
            acc +
            String.format("%9s", header[idx]) +
            list.joinToString(prefix = " ", postfix = "\t") { it.toString() } +
            "\n"
        }
        return headerStr + matStr
    }
}

fun makeCorrMatrix(dataList: List<Map<String, Double>>) : MatrixWithHeader {
    val means = dataList.reduce { acc, datum ->
        acc.mapValues { it.value + datum.getValue(it.key) }
    }.mapValues { it.value / dataList.count() }

    val vars = dataList.fold(means.mapValues { 0.0 }) { acc, datum ->
        acc.mapValues { it.value + (datum.getValue(it.key) - means.getValue(it.key)).pow(2) }
    }.mapValues { it.value / dataList.count() } // ここ標本分散なので -1してもいい

    val header = means.keys.toList()
    val matrix = 0.until(header.count())
        .map { mutableListOf<DoubleElement>() }
        .toMutableList()
    for ((rowIdx, row) in header.withIndex()) {
        for ((colIdx, col) in header.withIndex()) {
            when {
                rowIdx == colIdx -> matrix[rowIdx].add(DoubleElement(1.0))
                rowIdx > colIdx -> matrix[rowIdx].add(matrix[colIdx][rowIdx])
                else -> matrix[rowIdx].add(
                    DoubleElement(
                        dataList
                            .map { (it.getValue(row) - means.getValue(row)) * (it.getValue(col) - means.getValue(col)) }
                            .reduce { acc, d -> acc + d } /
                                (dataList.count() * sqrt(vars.getValue(row) * vars.getValue(col))) // ここ標本分散なので -1してもいい
                    )
                )
            }
        }
    }
    return MatrixWithHeader(
        header,
        Matrix(
            matrix.map { it.toList() }
        )
    )
}

const val loopMax = 50
const val minimumAbsValue = 0.000_000_1

data class EigenPair(val eigenValue: Double, val eigenVectors: List<Double>)

fun jacobi(mat: Matrix) : Pair<Int, List<EigenPair>> {
    var loopCount = 0
    var resultMatrix = mat
    var eigenVectors = mat.identity()
    while (true) {
        val maxIndexed = resultMatrix.comp
            .mapIndexed { rowIdx, row ->
                row.mapIndexed{ colIdx, ele -> Triple(rowIdx, colIdx, ele)}
                    .filter { it.first < it.second }
            }.flatten().maxBy { abs(it.third.value()) }!!
        if (loopCount > loopMax || minimumAbsValue > abs(maxIndexed.third.value())) break

        val givens = makeGivens(resultMatrix, maxIndexed)
        resultMatrix = givens.transposed() * resultMatrix * givens
        eigenVectors *= givens
        loopCount++

//        println("##### loop: ${loopCount}")
//        println("maxIndexed: ${maxIndexed.first}, ${maxIndexed.second}, ${maxIndexed.third}\n${resultMatrix}\n")
    }

    val eigenValues = resultMatrix.comp.mapIndexed { rowIdx, list -> list.filterIndexed { colIdx, ele -> colIdx == rowIdx }.first().value()}
    val eigenPairs = eigenValues.mapIndexed { index, d -> EigenPair(d, eigenVectors.row(index).map { it.value() }) }

    return Pair(loopCount, eigenPairs)
}

private fun makeGivens(matrix: Matrix, maxIndexed: Triple<Int, Int, MElement>): Matrix {

    val pp = matrix.comp[maxIndexed.first][maxIndexed.first].value()
    val pq = matrix.comp[maxIndexed.first][maxIndexed.second].value()
    val qq = matrix.comp[maxIndexed.second][maxIndexed.second].value()
    val radian = if (pp == qq) {
        PI / 4.0
    } else {
        val v = -2.0 * pq / (pp - qq)
        atan(v) / 2.0
    }

    val comp = matrix.identity().comp.mapIndexed { rowIdx, list ->
        list.mapIndexed { colIdx, ele ->
            when {
                rowIdx == maxIndexed.first && colIdx == maxIndexed.first -> DoubleElement(cos(radian))
                rowIdx == maxIndexed.first && colIdx == maxIndexed.second -> DoubleElement(sin(radian))
                rowIdx == maxIndexed.second && colIdx == maxIndexed.first -> DoubleElement(-1.0 * sin(radian))
                rowIdx == maxIndexed.second && colIdx == maxIndexed.second -> DoubleElement(cos(radian))
                else -> ele
            }
        }
    }
    return Matrix(
        comp
    )
} 
package model

import kotlin.math.pow
import kotlin.math.sqrt


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
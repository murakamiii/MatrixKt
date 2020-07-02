package model

import java.math.BigDecimal
import java.math.MathContext
import ch.obermuhlner.math.big.*
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

fun makeCorrMatrix(doubleDataList: List<Map<String, Double>>) : MatrixWithHeader {
    val dataList = doubleDataList.map { sdMap -> sdMap.mapValues { it.value.toBigDecimal() } }
    val means = dataList.reduce { acc, datum ->
        acc.mapValues { it.value + datum.getValue(it.key) }
    }.mapValues { it.value / dataList.count().toBigDecimal() }

    val vars = dataList.fold(means.mapValues { BigDecimal.ZERO }) { acc, datum ->
        acc.mapValues { it.value + (datum.getValue(it.key) - means.getValue(it.key)).pow(2) }
    }.mapValues { it.value / dataList.count().toBigDecimal() } // ここ標本分散なので -1してもいい

    val header = means.keys.toList()
    val matrix = 0.until(header.count())
        .map { mutableListOf<DecimalElement>() }
        .toMutableList()
    for ((rowIdx, row) in header.withIndex()) {
        for ((colIdx, col) in header.withIndex()) {
            when {
                rowIdx == colIdx -> matrix[rowIdx].add(DecimalElement(0))
                rowIdx > colIdx -> matrix[rowIdx].add(matrix[colIdx][rowIdx])
                else -> matrix[rowIdx].add(
                    DecimalElement(
                        dataList
                            .map { (it.getValue(row) - means.getValue(row)) * (it.getValue(col) - means.getValue(col)) }
                            .reduce { acc, d -> acc + d } /
                                (dataList.count().toBigDecimal() * (vars.getValue(row) * vars.getValue(col)).sqrt(
                                    MathContext.DECIMAL64)) // ここ標本分散なので -1してもいい
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

data class EigenPair(val eigenValue: BigDecimal, val eigenVectors: List<BigDecimal>)

fun jacobi(mat: Matrix) : Pair<Int, List<EigenPair>> {
    var loopCount = 0
    var resultMatrix = mat
    var eigenVectors = mat.identity()
    while (true) {
        val maxIndexed = resultMatrix.comp
            .mapIndexed { rowIdx, row ->
                row.mapIndexed{ colIdx, ele -> Triple(rowIdx, colIdx, ele)}
                    .filter { it.first < it.second }
            }.flatten().maxBy { (it.third.value()).abs() }!!
        if (loopCount > loopMax || minimumAbsValue.toBigDecimal() > (maxIndexed.third.value()).abs()) break

        val givens = makeGivens(resultMatrix, maxIndexed)
        resultMatrix = givens.transposed() * resultMatrix * givens
        eigenVectors *= givens
        loopCount++

//        println("##### loop: ${loopCount}")
//        println("maxIndexed: ${maxIndexed.first}, ${maxIndexed.second}, ${maxIndexed.third}\n${resultMatrix}\n")
    }

    val eigenValues = resultMatrix.comp.mapIndexed { rowIdx, list -> list.filterIndexed { colIdx, ele -> colIdx == rowIdx }.first().value()}
    val eigenPairs = eigenValues.mapIndexed { index, d -> EigenPair(d, eigenVectors.col(index).map { it.value() }) }

    return Pair(loopCount, eigenPairs)
}

private fun makeGivens(matrix: Matrix, maxIndexed: Triple<Int, Int, MElement>): Matrix {

    val pp = matrix.comp[maxIndexed.first][maxIndexed.first].value()
    val pq = matrix.comp[maxIndexed.first][maxIndexed.second].value()
    val qq = matrix.comp[maxIndexed.second][maxIndexed.second].value()
    val radian = if (pp == qq) {
        PI.toBigDecimal() / 4.toBigDecimal()
    } else {
        val v = (-2.0).toBigDecimal() * pq / (pp - qq)
        BigDecimalMath.atan(v, MathContext.DECIMAL64) / 2.toBigDecimal()
    }

    val comp = matrix.identity().comp.mapIndexed { rowIdx, list ->
        list.mapIndexed { colIdx, ele ->
            when {
                rowIdx == maxIndexed.first && colIdx == maxIndexed.first -> DecimalElement(BigDecimalMath.cos(radian, MathContext.DECIMAL64))
                rowIdx == maxIndexed.first && colIdx == maxIndexed.second -> DecimalElement(BigDecimalMath.sin(radian, MathContext.DECIMAL64))
                rowIdx == maxIndexed.second && colIdx == maxIndexed.first -> DecimalElement((-1.0).toBigDecimal() * BigDecimalMath.sin(radian, MathContext.DECIMAL64))
                rowIdx == maxIndexed.second && colIdx == maxIndexed.second -> DecimalElement(BigDecimalMath.cos(radian, MathContext.DECIMAL64))
                else -> ele
            }
        }
    }
    return Matrix(
        comp
    )
}

data class ImportanceComponents(val standardDeviation: BigDecimal, val proportionOfVariance: BigDecimal, val cumulativeProportion: BigDecimal)
data class PCAResult(val importance: List<ImportanceComponents>, val rotation: List<Map<String, BigDecimal>>) {
    override fun toString(): String {
        val iTitle = "Importance of components:\n\n"
        var iHeader = "                      "
        var sd = "Standard deviation    "
        var pv = "Proportion of Variance"
        var cp = "Cumulative Proportion "
        importance.forEachIndexed{ idx,  it ->
            iHeader +=  "    PC${idx + 1}"
            sd += String.format(" %.4f", it.standardDeviation)
            pv += String.format(" %.4f", it.proportionOfVariance)
            cp += String.format(" %.4f", it.cumulativeProportion)
        }

        val maxKeyLength = rotation.first().keys.maxBy { it.length }!!.length
        var rHeader = " ".repeat(maxKeyLength + 1)
        println("rHeader: ${rHeader.length}")
        var ro = rotation.first().keys.map { it to "" }.toMap()
        rotation.forEachIndexed { idx, map ->
            rHeader += "PC${idx + 1}".padStart(9)
            ro = ro.mapValues { it.value.plus(if (map[it.key]!! > BigDecimal.ZERO) " " else "") + String.format(" %.5f", map[it.key]) }
        }
        val roStr = ro.map { "${it.key.padStart(maxKeyLength)} ${it.value}\n" }.joinToString(separator = "")
        return iTitle + iHeader + "\n" + sd + "\n" + pv + "\n" + cp + "\n\n" + rHeader + "\n" + roStr
    }
}

fun pcaComponent(result: Pair<Int, List<EigenPair>>, header: List<String>) : PCAResult {
    val eigens = result.second

    val sds = eigens.sortedByDescending { it.eigenValue }

    val sum =  sds.map { it.eigenValue }.reduce { acc, bigDecimal -> acc + bigDecimal }

    val ics = sds.fold(emptyList<ImportanceComponents>()) { acc, ele ->
        if (acc.isEmpty()) {
            listOf(ImportanceComponents(
                ele.eigenValue.sqrt(MathContext.DECIMAL64),
                ele.eigenValue / sum,
                ele.eigenValue / sum)
            )
        } else {
            acc.plus(
                ImportanceComponents(
                    ele.eigenValue.sqrt(MathContext.DECIMAL64),
                    ele.eigenValue / sum,
                    acc.last().cumulativeProportion + (ele.eigenValue / sum)
                )
            )
        }
    }
    val rotation = sds.foldIndexed(emptyList<Map<String, BigDecimal>>()) { pcIdx, acc, ele ->
        acc.plus(header.mapIndexed { idx, s ->  s to sds[pcIdx].eigenVectors[idx] }.toMap())
    }
    return PCAResult(ics, rotation)
}
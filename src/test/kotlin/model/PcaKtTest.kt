package model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
/*
    しんどいのでスモークテストのみ
 */

internal class PcaKtTest {

    @Test
    fun makeCorrMatrix() {
        // https://qiita.com/NoriakiOshita/items/460247bb57c22973a5f0
        val testlist = listOf(
            mapOf("Python" to 70.0, "Java" to 30.0, "fee" to 700.0),
            mapOf("Python" to 32.0, "Java" to 60.0, "fee" to 480.0),
            mapOf("Python" to 32.0, "Java" to 20.0, "fee" to 300.0),
            mapOf("Python" to 20.0, "Java" to 120.0, "fee" to 600.0),
            mapOf("Python" to 40.0, "Java" to 120.0, "fee" to 630.0),
            mapOf("Python" to 40.0, "Java" to 30.0, "fee" to 520.0),
            mapOf("Python" to 300.0, "Java" to 1100.0, "fee" to 1200.0),
            mapOf("Python" to 2000.0, "Java" to 400.0, "fee" to 1500.0),
            mapOf("Python" to 40.0, "Java" to 180.0, "fee" to 800.0)
        )
        val hmat = makeCorrMatrix(testlist)
        assertEquals(listOf("Python", "Java", "fee"), hmat.header)

        val expectedMatrix = Matrix(
            listOf(
                listOf(
                    DecimalElement(1),
                    DecimalElement(0.31125089218960195.toBigDecimal()),
                    DecimalElement(0.8181136457904.toBigDecimal())
                ),
                listOf(
                    DecimalElement(0.31125089218960195.toBigDecimal()),
                    DecimalElement(1),
                    DecimalElement(0.7093174088556866.toBigDecimal())
                ),
                listOf(
                    DecimalElement(0.8181136457904.toBigDecimal()),
                    DecimalElement(0.7093174088556866.toBigDecimal()),
                    DecimalElement(1)
                )
            )
        )
        assertEquals(expectedMatrix, hmat.value)
    }

    @Test
    fun jacobi() {
        // http://nkl.cc.u-tokyo.ac.jp/16n/Eigen.pdf
        val mat = Matrix(
            listOf(
                listOf(
                    DecimalElement(1),
                    DecimalElement(3),
                    DecimalElement(3),
                    DecimalElement(1)
                ),
                listOf(
                    DecimalElement(3),
                    DecimalElement(2),
                    DecimalElement(0),
                    DecimalElement(2)
                ),
                listOf(
                    DecimalElement(3),
                    DecimalElement(0),
                    DecimalElement(3),
                    DecimalElement(1)
                ),
                listOf(
                    DecimalElement(1),
                    DecimalElement(2),
                    DecimalElement(1),
                    DecimalElement(1)
                )
            )
        )
        val jacpbied = jacobi(mat)
        assertEquals(4, jacpbied.second.count())
        assertTrue(((-2.717).toBigDecimal() - jacpbied.second[0].eigenValue).abs() < 0.001.toBigDecimal())
        assertTrue((6.934.toBigDecimal() - jacpbied.second[1].eigenValue).abs() < 0.001.toBigDecimal())
        assertTrue((2.745.toBigDecimal() - jacpbied.second[2].eigenValue).abs() < 0.001.toBigDecimal())
        assertTrue((0.038_67.toBigDecimal() - jacpbied.second[3].eigenValue).abs() < 0.000_01.toBigDecimal())
        assertTrue((7.073E-01.toBigDecimal() - jacpbied.second[0].eigenVectors[0]).abs() < 0.000_1.toBigDecimal())
        assertTrue((5.822E-01.toBigDecimal() - jacpbied.second[1].eigenVectors[0]).abs() < 0.000_1.toBigDecimal())
        assertTrue((2.994E-02.toBigDecimal() - jacpbied.second[2].eigenVectors[0]).abs() < 0.000_01.toBigDecimal())
        assertTrue((-3.999E-01.toBigDecimal() - jacpbied.second[3].eigenVectors[0]).abs() < 0.000_1.toBigDecimal())
        assertTrue((-5.384E-01.toBigDecimal() - jacpbied.second[0].eigenVectors[1]).abs() < 0.000_1.toBigDecimal())
        assertTrue((4.984E-01.toBigDecimal() - jacpbied.second[1].eigenVectors[1]).abs() < 0.000_1.toBigDecimal())
        assertTrue((-6.222E-01.toBigDecimal() - jacpbied.second[2].eigenVectors[1]).abs() < 0.000_1.toBigDecimal())
        assertTrue((-2.732E-01.toBigDecimal() - jacpbied.second[3].eigenVectors[1]).abs() < 0.000_1.toBigDecimal())
        assertTrue((-4.077E-01.toBigDecimal() - jacpbied.second[0].eigenVectors[2]).abs() < 0.000_1.toBigDecimal())
        assertTrue((5.345E-01.toBigDecimal() - jacpbied.second[1].eigenVectors[2]).abs() < 0.000_1.toBigDecimal())
        assertTrue((7.318E-01.toBigDecimal() - jacpbied.second[2].eigenVectors[2]).abs() < 0.000_1.toBigDecimal())
        assertTrue((1.121E-01.toBigDecimal() - jacpbied.second[3].eigenVectors[2]).abs() < 0.000_1.toBigDecimal())
        assertTrue((2.091E-01.toBigDecimal() - jacpbied.second[0].eigenVectors[3]).abs() < 0.000_1.toBigDecimal())
        assertTrue((3.562E-01.toBigDecimal() - jacpbied.second[1].eigenVectors[3]).abs() < 0.000_1.toBigDecimal())
        assertTrue((-2.766E-01.toBigDecimal() - jacpbied.second[2].eigenVectors[3]).abs() < 0.000_1.toBigDecimal())
        assertTrue((8.677E-01.toBigDecimal() - jacpbied.second[3].eigenVectors[3]).abs() < 0.000_1.toBigDecimal())
    }

    @Test
    fun pcaComponent() {
        val testlist = listOf(
            mapOf("Python" to 70.0, "Java" to 30.0, "fee" to 700.0),
            mapOf("Python" to 32.0, "Java" to 60.0, "fee" to 480.0),
            mapOf("Python" to 32.0, "Java" to 20.0, "fee" to 300.0),
            mapOf("Python" to 20.0, "Java" to 120.0, "fee" to 600.0),
            mapOf("Python" to 40.0, "Java" to 120.0, "fee" to 630.0),
            mapOf("Python" to 40.0, "Java" to 30.0, "fee" to 520.0),
            mapOf("Python" to 300.0, "Java" to 1100.0, "fee" to 1200.0),
            mapOf("Python" to 2000.0, "Java" to 400.0, "fee" to 1500.0),
            mapOf("Python" to 40.0, "Java" to 180.0, "fee" to 800.0)
        )
        val hmat = makeCorrMatrix(testlist)
        val res = jacobi(hmat.value)
        val pca = pcaComponent(res, hmat.header)

        assertTrue((1.4995.toBigDecimal() - pca.importance[0].standardDeviation).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.8322.toBigDecimal() - pca.importance[1].standardDeviation).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.2430.toBigDecimal() - pca.importance[2].standardDeviation).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.7495.toBigDecimal() - pca.importance[0].proportionOfVariance).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.2308.toBigDecimal() - pca.importance[1].proportionOfVariance).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.0197.toBigDecimal() - pca.importance[2].proportionOfVariance).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.7495.toBigDecimal() - pca.importance[0].cumulativeProportion).abs() < 0.000_1.toBigDecimal())
        assertTrue((0.9803.toBigDecimal() - pca.importance[1].cumulativeProportion).abs() < 0.000_1.toBigDecimal())
        assertTrue((1.0000.toBigDecimal() - pca.importance[2].cumulativeProportion).abs() < 0.000_1.toBigDecimal())

        assertTrue((0.55664.toBigDecimal() - (pca.rotation[0].getValue("Python")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.51094.toBigDecimal() - (pca.rotation[0].getValue("Java")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.65505.toBigDecimal() - (pca.rotation[0].getValue("fee")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.64384.toBigDecimal() - (pca.rotation[1].getValue("Python")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.76362.toBigDecimal() - (pca.rotation[1].getValue("Java")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.04853.toBigDecimal() - (pca.rotation[1].getValue("fee")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.52501.toBigDecimal() - (pca.rotation[2].getValue("Python")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.39474.toBigDecimal() - (pca.rotation[2].getValue("Java")).abs()).abs() < 0.000_01.toBigDecimal())
        assertTrue((0.75402.toBigDecimal() - (pca.rotation[2].getValue("fee")).abs()).abs() < 0.000_01.toBigDecimal())
    }
}

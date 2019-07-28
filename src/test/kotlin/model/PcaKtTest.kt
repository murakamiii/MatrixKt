package model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
        assertEquals(listOf("Python","Java","fee"), hmat.header)

        val expectedMatrix = Matrix(
            listOf(
                listOf(
                    DoubleElement(1.0),
                    DoubleElement(0.31125089218960195),
                    DoubleElement(0.8181136457904)
                ),
                listOf(
                    DoubleElement(0.31125089218960195),
                    DoubleElement(1.0),
                    DoubleElement(0.7093174088556866)
                ),
                listOf(
                    DoubleElement(0.8181136457904),
                    DoubleElement(0.7093174088556866),
                    DoubleElement(1.0)
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
                    DoubleElement(1.0),
                    DoubleElement(3.0),
                    DoubleElement(3.0),
                    DoubleElement(1.0)
                ),
                listOf(
                    DoubleElement(3.0),
                    DoubleElement(2.0),
                    DoubleElement(0.0),
                    DoubleElement(2.0)
                ),
                listOf(
                    DoubleElement(3.0),
                    DoubleElement(0.0),
                    DoubleElement(3.0),
                    DoubleElement(1.0)
                ),
                listOf(
                    DoubleElement(1.0),
                    DoubleElement(2.0),
                    DoubleElement(1.0),
                    DoubleElement(1.0)
                )
            )
        )
        val jacpbied = jacobi(mat)
        assertEquals(4, jacpbied.second.count())
        assertEquals(-2.717, jacpbied.second[0].eigenValue, 0.001)
        assertEquals(6.934, jacpbied.second[1].eigenValue, 0.001)
        assertEquals(2.745, jacpbied.second[2].eigenValue, 0.001)
        assertEquals(0.038_67, jacpbied.second[3].eigenValue, 0.000_01)

        assertEquals(7.073E-01, jacpbied.second[0].eigenVectors[0], 0.000_1)
        assertEquals(5.822E-01, jacpbied.second[0].eigenVectors[1], 0.000_1)
        assertEquals(2.994E-02, jacpbied.second[0].eigenVectors[2], 0.000_01)
        assertEquals(-3.999E-01, jacpbied.second[0].eigenVectors[3], 0.000_1)

        assertEquals(-5.384E-01, jacpbied.second[1].eigenVectors[0], 0.000_1)
        assertEquals(4.984E-01, jacpbied.second[1].eigenVectors[1], 0.000_1)
        assertEquals(-6.222E-01, jacpbied.second[1].eigenVectors[2], 0.000_1)
        assertEquals(-2.732E-01, jacpbied.second[1].eigenVectors[3], 0.000_1)

        assertEquals(-4.077E-01, jacpbied.second[2].eigenVectors[0], 0.000_1)
        assertEquals(5.345E-01, jacpbied.second[2].eigenVectors[1], 0.000_1)
        assertEquals(7.318E-01, jacpbied.second[2].eigenVectors[2], 0.000_1)
        assertEquals(1.121E-01, jacpbied.second[2].eigenVectors[3], 0.000_1)

        assertEquals(2.091E-01, jacpbied.second[3].eigenVectors[0], 0.000_1)
        assertEquals(3.562E-01, jacpbied.second[3].eigenVectors[1], 0.000_1)
        assertEquals(-2.766E-01, jacpbied.second[3].eigenVectors[2], 0.000_1)
        assertEquals(8.677E-01, jacpbied.second[3].eigenVectors[3], 0.000_1)
    }
}
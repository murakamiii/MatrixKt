package model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/*
    しんどいのでスモークテストのみ
 */
internal class PcaKtTest {

    @Test
    fun makeCorrMatrix() {
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
}
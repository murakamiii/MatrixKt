import model.jacobi
import model.makeCorrMatrix
import model.pcaComponent

fun main() {

//    var fileReader: BufferedReader? = null
//    val perser = CSVParser(fileReader,
//        CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())
//    val first: CSVRecord = perser.records.first()
//    first.toMap()

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
    println(hmat)

    val res = jacobi(hmat.value)

    println("ループ数: ${res.first}")
    println("結果:\n ${res.second}")

    val pca = pcaComponent(res, hmat.header)
    println(pca)
}

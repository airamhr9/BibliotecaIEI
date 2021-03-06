import extractors.ExtractorCAT
import extractors.ExtractorCV
import extractors.ExtractorEUS
import extractors.Ubicacion
import persistence.DataWarehouse
import wrappers.WrapperCAT
import wrappers.WrapperCV

fun main(args: Array<String>) {

    // Argumentos de entrada
    val jsonFileEUS = args[0]
    val xmlFileCAT = args[1]
    val jsonFileCAT = args[2]
    val csvFileCV = args[3]
    val jsonFileCV = args[4]

    val chromeDriverPath = args[5]

    println("Fuente de datos de Euskadi: $jsonFileEUS")
    println("Fuente de datos de Cataluña: $xmlFileCAT $jsonFileCAT")
    println("Fuente de datos de la Comunidad Valenciana: $csvFileCV $jsonFileCV")
    println("Chrome driver: $chromeDriverPath\n")

    // Vaciar las tablas de la BD
    DataWarehouse.deleteTables()
    var bibliotecas = DataWarehouse.getBibliotecas()
    if (bibliotecas.isEmpty()) {
        println("La base da datos no contiene ninguna biblioteca\n")
    }

    // Procesar datos de Euskadi
    val extractorEUS = ExtractorEUS(jsonFileEUS)
    extractorEUS.extraerDatos()

    // Procesar datos de Cataluña
    val wrapperCAT = WrapperCAT(xmlFileCAT, jsonFileCAT)
    wrapperCAT.createJsonFile()
    val extractorCAT = ExtractorCAT(jsonFileCAT)
    extractorCAT.extraerDatos()

    // Procesar datos de la Comunidad Valenciana
    val wrapperCV = WrapperCV(csvFileCV, jsonFileCV)
    wrapperCV.createJsonFile()
    Ubicacion(chromeDriverPath).use { ubicacion ->
        val extractorCV = ExtractorCV(jsonFileCV, ubicacion)
        extractorCV.extraerDatos()
    }

    // Imprimir las bibliotecas de la BD
    println()
    bibliotecas = DataWarehouse.getBibliotecas()
    bibliotecas.forEach { println(it) }
    println("\nNúmero de bibliotecas: ${bibliotecas.size}")

}
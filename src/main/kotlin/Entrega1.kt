import extractors.ExtractorCAT
import extractors.ExtractorCV
import extractors.ExtractorEUS
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
    println("Chrome driver: $chromeDriverPath")

    // Vaciar las tablas de la BD
    val dataWarehouse = DataWarehouse.getInstance()
    dataWarehouse.deleteTables()

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
    val extractorCV = ExtractorCV(jsonFileCV) // Pasar path del driver u objeto Ubicacion (y despues cerrarlo)
    extractorCV.extraerDatos()

    // Imprimir las bibliotecas de la BD
    val bibliotecas = dataWarehouse.getBibliotecas()
    bibliotecas.forEach { println(it) }

}
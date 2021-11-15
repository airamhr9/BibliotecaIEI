package persistence

import extractors.ExtractorCAT
import wrappers.WrapperCAT

fun main() {
//val xmlFile = "C:\\Users\\hecto\\Desktop\\biblioteques.xml"
    val xmlFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Catalunya/biblioteques.xml"
val jsonFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Catalunya/biblioteques.json"

    val wrapper = WrapperCAT(xmlFile, jsonFile)
    wrapper.createJsonFile()

    val extractorCAT = ExtractorCAT(jsonFile)
    extractorCAT.extraerDatos()

    val dataWarehouse = DataWarehouse.getInstance()
    val bibliotecas = dataWarehouse.getBibliotecas()
    bibliotecas.forEach { println(it) }
}
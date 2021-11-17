package persistence

import extractors.ExtractorEUS

fun main() {

    //val xmlFile = "C:\\Users\\hecto\\Desktop\\biblioteques.xml"
    val jsonFile = "C:\\Users\\hecto\\Desktop\\bibliotecas.json"
    //val xmlFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Catalunya/biblioteques.xml"
    //val jsonFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Catalunya/biblioteques.json"

    /*val wrapper = WrapperCAT(xmlFile, jsonFile)
    wrapper.createJsonFile()*/

    val extractorEUS = ExtractorEUS(jsonFile)
    extractorEUS.extraerDatos()

    val dataWarehouse = DataWarehouse.getInstance()
    val bibliotecas = dataWarehouse.getBibliotecas()
    bibliotecas.forEach { println(it) }
}
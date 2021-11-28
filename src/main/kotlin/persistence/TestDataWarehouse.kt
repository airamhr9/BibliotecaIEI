package persistence

import extractors.ExtractorEUS
import wrappers.WrapperCV

fun main() {

    val xmlFile = "C:\\Users\\hecto\\Desktop\\biblioteques.xml"
    //val jsonFile = "C:\\Users\\hecto\\Desktop\\bibliotecas.json"
    //val xmlFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Catalunya/biblioteques.xml"
    //val jsonFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Catalunya/biblioteques.json"

   //CV
    //val csvFile = "C:\\Users\\Jaime\\Documents\\FuentesIEI\\CV\\valencia.csv"
    //val jsonFile = "C:\\Users\\Jaime\\Documents\\FuentesIEI\\SalidaJson\\salida.json"


    /*val wrapper = WrapperCAT(xmlFile, jsonFile)
    wrapper.createJsonFile()*/

   /* val extractorEUS = ExtractorEUS(jsonFile)
    extractorEUS.extraerDatos()*/



    //val wrapper = WrapperCV(csvFile,jsonFile)
    //wrapper.createJsonFile()

    val jsonFile = "/home/airam/Documentos/Proyecto/Fuentes de datos/Euskadi/bibliotecas.json"
    val extractorEUS = ExtractorEUS(jsonFile)
    extractorEUS.extraerDatos()

    val bibliotecas = DataWarehouse.getBibliotecas()
    bibliotecas.forEach { println(it) }
}
package server

import extractors.ExtractorCAT
import extractors.ExtractorCV
import extractors.ExtractorEUS
import extractors.Ubicacion
import objects.Biblioteca
import persistence.DataWarehouse
import wrappers.WrapperCAT
import wrappers.WrapperCV

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/") // http://127.0.0.1:8080/
class BibliotecaController {

    @GetMapping("/load") // /load?sources=eus&cat&cv
    fun cargarBibliotecas(@RequestParam sources: String): String {
        DataWarehouse.deleteTables()
        val listaDeFuentes = sources.split('&')
        seleccionarFuentes(listaDeFuentes)
        return "Las bibliotecas se han cargado en el almacén de datos correctamente"
    }

    @GetMapping("/search") // /search?localidad={localidad}&codigoPostal={codigoPostal}&provincia={provincia}&tipo={tipo}
    fun buscarBibliotecas(
        @RequestParam localidad: String,
        // @RequestParam codigoPostal: String,
        // @RequestParam provincia: String,
        // @RequestParam tipo: String
    ): List<Biblioteca> {
        val todasLasBibliotecas = DataWarehouse.getBibliotecas()

        // Falta utilizar el resto de parametros y comprobar si son nulos
        val result = todasLasBibliotecas.filter {
            it.enLocalidad.nombre == localidad
                    // && it.codigoPostal == codigoPostal
                    // && it.enLocalidad.enProvincia.nombre == provincia
                    // && it.tipo == tipo
        }

        return result
    }

    private fun seleccionarFuentes(fuentes: List<String>) {
        if (fuentes.contains("cat")) {
            insertarBibliotecasDeCatalunya()
        }
        if (fuentes.contains("cv")) {
            insertarBibliotecasDeComunitatValenciana()
        }
        if (fuentes.contains("eus")) {
            insertarBibliotecasDeEuskadi()
        }
    }

    private fun insertarBibliotecasDeCatalunya() {
        val wrapperCAT = WrapperCAT(Server.xmlFileCAT, Server.jsonFileCAT)
        wrapperCAT.createJsonFile()
        val extractorCAT = ExtractorCAT(Server.jsonFileCAT)
        extractorCAT.extraerDatos()
    }

    private fun insertarBibliotecasDeComunitatValenciana() {
        val wrapperCV = WrapperCV(Server.csvFileCV, Server.jsonFileCV)
        wrapperCV.createJsonFile()
        Ubicacion(Server.chromeDriverPath).use { ubicacion ->
            val extractorCV = ExtractorCV(Server.jsonFileCV, ubicacion)
            extractorCV.extraerDatos()
        }
    }

    private fun insertarBibliotecasDeEuskadi() {
        val extractorEUS = ExtractorEUS(Server.jsonFileEUS)
        extractorEUS.extraerDatos()
    }

}
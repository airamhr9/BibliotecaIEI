package extractors

import java.util.*

abstract class Extractor(val jsonFile: String) {

    abstract fun extraerDatos()

    fun String.duplicarApostrofos(): String {
        return this.replace("'", "''")
    }

    private fun generarIdentificador(): String {
        return UUID.randomUUID().toString()
    }

    protected fun generarIdentificadorDeLocalidad(
        codigosDeLocalidad: MutableMap<String, String>,
        localidad: String
    ): String {
        return if (codigosDeLocalidad.containsKey(localidad)) {
            codigosDeLocalidad[localidad]!!
        } else {
            val id = generarIdentificador()
            codigosDeLocalidad[localidad] = id
            id
        }
    }

}
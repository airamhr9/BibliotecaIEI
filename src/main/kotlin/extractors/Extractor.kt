package extractors

import java.util.*

abstract class Extractor(val jsonFile: String) {

    abstract fun extraerDatos()

    fun String.duplicarApostrofos(): String {
        return this.replace("'", "''")
    }

    protected fun generarIdentificadorDeLocalidad(): String {
        return UUID.randomUUID().toString()
    }

}
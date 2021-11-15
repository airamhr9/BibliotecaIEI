package extractors

import persistence.DataWarehouse

abstract class Extractor(val jsonFile: String) {

    val dataWarehouse = DataWarehouse.getInstance()

    abstract fun extraerDatos()

    fun String.duplicarApostrofos(): String {
        return this.replace("'", "''")
    }

}
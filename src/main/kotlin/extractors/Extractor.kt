package extractors

abstract class Extractor(val jsonFile: String) {

    abstract fun extraerDatos()

    fun String.duplicarApostrofos(): String {
        return this.replace("'", "''")
    }

}
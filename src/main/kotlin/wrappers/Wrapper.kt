package wrappers

abstract class Wrapper (
    val sourceFile: String,
    val jsonFile: String
    ) {

    abstract fun createJsonFile()

}
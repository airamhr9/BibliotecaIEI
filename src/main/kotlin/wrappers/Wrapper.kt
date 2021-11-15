package wrappers

abstract class Wrapper(val sourceFile: String, val jsonFile: String) {

    abstract fun createJsonFile()

    companion object {
        const val indentFactor = 4
    }

}
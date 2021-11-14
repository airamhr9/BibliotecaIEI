package wrappers

import org.json.XML
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

class WrapperCAT(xmlFile: String, jsonFile: String) : Wrapper(xmlFile, jsonFile) {

    override fun createJsonFile() {
        val data = String(Files.readAllBytes(Paths.get(sourceFile)))
        val jsonObject = XML.toJSONObject(data)
        FileWriter(jsonFile).use { fileWriter ->
            fileWriter.write(jsonObject.toString(indentFactor))
        }
    }

}


package wrappers

import org.json.XML
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

class WrapperCAT(xmlFile: String, jsonFile: String) : Wrapper(xmlFile, jsonFile) {

    override fun createJsonFile() {
        val prettyPrintIndentFactor = 4
        val xmlString = String(Files.readAllBytes(Paths.get(sourceFile)))
        val xmlJSONObj = XML.toJSONObject(xmlString)
        FileWriter(jsonFile).use { fileWriter ->
            fileWriter.write(xmlJSONObj.toString(prettyPrintIndentFactor))
        }
    }

}


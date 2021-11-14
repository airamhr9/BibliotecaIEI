package wrappers

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.PrintWriter
import java.util.*

class WrapperCV(csvFile: String, jsonFile: String) : Wrapper(csvFile, jsonFile) {

    override fun createJsonFile() {
        val jsonArray = readData()
        val data = jsonArray.toString(indentFactor)
        writeFile(data)
    }

    private fun readData(): JSONArray {
        val jsonArray = JSONArray()
        Scanner(File(sourceFile)).use { scanner ->
            scanner.nextLine() // Fila con los nombres de columna
            while (scanner.hasNextLine()) {
                val row = scanner.nextLine()
                val data = row.split(';')
                val jsonObject = makeJsonObject(data)
                jsonArray.put(jsonObject)
            }
        }
        return jsonArray
    }

    private fun makeJsonObject(data: List<String>): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("COD_PROVINCIA", data[COD_PROVINCIA])
        jsonObject.put("NOM_PROVINCIA", data[NOM_PROVINCIA])
        jsonObject.put("COD_MUNICIPIO", data[COD_MUNICIPIO])
        jsonObject.put("NOM_MUNICIPIO", data[NOM_MUNICIPIO])
        jsonObject.put("TIPO", data[TIPO])
        jsonObject.put("NOMBRE", data[NOMBRE])
        jsonObject.put("DIRECCION", data[DIRECCION])
        jsonObject.put("CP", data[CP])
        jsonObject.put("TELEFONO", data[TELEFONO])
        jsonObject.put("FAX", data[FAX])
        jsonObject.put("WEB", data[WEB])
        jsonObject.put("CATALOGO", data[CATALOGO])
        jsonObject.put("EMAIL", data[EMAIL])
        jsonObject.put("CENTRAL", data[CENTRAL])
        jsonObject.put("COD_CARACTER", data[COD_CARACTER])
        jsonObject.put("DESC_CARACTER", data[DESC_CARACTER])
        jsonObject.put("DECRETO", data[DECRETO])
        return jsonObject
    }

    private fun writeFile(data: String) {
        PrintWriter(jsonFile).use { printWriter ->
            printWriter.print(data)
        }
    }

    companion object {
        private val COD_PROVINCIA = 0
        private val NOM_PROVINCIA = 1
        private val COD_MUNICIPIO = 2
        private val NOM_MUNICIPIO = 3
        private val TIPO = 4
        private val NOMBRE = 5
        private val DIRECCION = 6
        private val CP = 7
        private val TELEFONO = 8
        private val FAX = 9
        private val WEB = 10;
        private val CATALOGO = 11;
        private val EMAIL = 12
        private val CENTRAL = 13
        private val COD_CARACTER = 14
        private val DESC_CARACTER = 15
        private val DECRETO = 16
    }

}
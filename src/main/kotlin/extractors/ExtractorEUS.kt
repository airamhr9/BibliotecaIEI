package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import org.json.JSONArray
import org.json.JSONObject
import persistence.DataWarehouse
import java.nio.file.Files
import java.nio.file.Paths

class ExtractorEUS(jsonFile: String) : Extractor(jsonFile) {

    override fun extraerDatos() {
        val jsonString = String(Files.readAllBytes(Paths.get(jsonFile)))
        val jsonArray = JSONArray(jsonString)

        jsonArray .forEach {
            val element = it as JSONObject

            val nombreBiblioteca = element.getString("documentName")
            val direccion = element.getString("address")
            val longitud = element.getDouble("lonwgs84")
            val latitud = element.getDouble("latwgs84")
            val email = element.getString("email")
            val codigoPostal = getPostalCode(element.getString("postalcode"))
            val telefono = obtenerTelefono(element)
            val descripcion = element.getString("documentDescription")
            val tipo = Titularidad.Publica

            val codigoProvincia = codigoPostal.substring(0,2)
            val nombreProvincia = element.getString("territory")

            var nombreLocalidad = element.getString("municipality")
            if(nombreLocalidad == "Vitoria-Gasteiz"){
                nombreLocalidad = "Vitoria - Gasteiz"
            }

            val codigoLocalidad = generarIdentificadorDeLocalidad(codigosDeLocalidadEUS, nombreLocalidad + codigoProvincia)

            val provincia = Provincia(nombreProvincia, codigoProvincia)
            val localidad = Localidad(nombreLocalidad, codigoLocalidad, provincia)
            val biblioteca = Biblioteca(nombreBiblioteca, tipo, direccion, codigoPostal, longitud, latitud,
                telefono, email, descripcion, localidad)

            DataWarehouse.addBiblioteca(biblioteca)
        }
    }

    private fun getPostalCode(postalCode1: String) : String{
        var postalCode = postalCode1
        if (postalCode[2] == '.') {
            postalCode = postalCode.substring(0,2) + postalCode.substring(3)
        }
        return postalCode
    }

    private fun obtenerTelefono(data: JSONObject): String {
        return data.getString("phone").replace(" ", "").take(9)
    }

    companion object {
        private val codigosDeLocalidadEUS = mutableMapOf<String, String>()
    }

}
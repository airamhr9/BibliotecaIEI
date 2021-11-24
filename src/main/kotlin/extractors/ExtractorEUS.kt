package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.nio.file.Files
import java.nio.file.Paths
import javax.sound.sampled.AudioFormat


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
            val telefono = element.getString("phone")
            val descripcion = element.getString("documentDescription")
            val tipo = Titularidad.Publica

            val codigoProvincia = codigoPostal.substring(0,2)
            val nombreProvincia = element.getString("territory")

            var nombreLocalidad = element.getString("municipality")
            if(nombreLocalidad == "Vitoria-Gasteiz"){
                nombreLocalidad = "Vitoria - Gasteiz"
            }
            val codigoLocalidad = nombreLocalidad + codigoProvincia


            val provincia = Provincia(nombreProvincia, codigoProvincia)
            val localidad = Localidad(nombreLocalidad, codigoLocalidad, provincia)
            val biblioteca = Biblioteca(nombreBiblioteca, tipo, direccion, codigoPostal, longitud, latitud,
                telefono, email, descripcion, localidad)

            dataWarehouse.addBiblioteca(biblioteca)
        }
    }

    private fun getPostalCode(postalCode1 : String) : String{
        var postalCode = postalCode1
        if(postalCode[2].equals('.')){
            postalCode = postalCode.substring(0,2) + postalCode.substring(3)
        }
        return  postalCode
    }

}
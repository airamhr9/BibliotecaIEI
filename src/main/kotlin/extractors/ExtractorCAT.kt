package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import persistence.DataWarehouse
import java.nio.file.Files
import java.nio.file.Paths

class ExtractorCAT(jsonFile: String) : Extractor(jsonFile) {

    override fun extraerDatos() {
        val jsonString = String(Files.readAllBytes(Paths.get(jsonFile)))
        val jsonObject = JSONObject(JSONTokener(jsonString))
        val jsonArray = jsonObject.getJSONObject("response").getJSONArray("row")

        jsonArray.forEach {
            val element = it as JSONObject

            val nombreBiblioteca = element.getString("nom").duplicarApostrofos()
            val direccion = element.getString("via").duplicarApostrofos()
            val longitud = element.getDouble("longitud")
            val latitud = element.getDouble("latitud")
            val email = element.getString("email")
            val nombreLocalidad = element.getString("poblacio").duplicarApostrofos()

            val codigoPostal = obtenerCodigoPostal(element)
            val telefono = obtenerTelefono(element)
            val descripcion = obtenerDescripcion(element)
            val tipo = obtenerTitularidad(descripcion)
            val codigoLocalidad = obtenerCodigoLocalidad(element)
            val codigoProvincia = obtenerCodigoProvincia(codigoPostal)
            val nombreProvincia = obtenerNombreProvincia(codigoProvincia)

            val provincia = Provincia(nombreProvincia, codigoProvincia)
            val localidad = Localidad(nombreLocalidad, codigoLocalidad, provincia)
            val biblioteca = Biblioteca(nombreBiblioteca, tipo, direccion, codigoPostal, longitud, latitud,
                telefono, email, descripcion, localidad)

            DataWarehouse.addBiblioteca(biblioteca)
        }
    }

    private fun obtenerTitularidad(propiedades: String): Titularidad {
        // En algunos casos no existen las propiedades
        return if (propiedades == "") {
            Titularidad.Publica
        } else {
            val propietatTitularitat = propiedades.substring(propiedades.lastIndexOf('|'))
            when (propietatTitularitat) {
                "Privada" -> Titularidad.Privada
                else -> Titularidad.Publica
            }
        }
    }

    private fun obtenerCodigoPostal(data: JSONObject): String {
        // Algunos codigos postales son string y otros int
        return try {
            data.getString("cpostal")
        } catch (ex: JSONException) {
            var codigoPostal = data.getInt("cpostal").toString()
            if (codigoPostal.startsWith("8")) {
                // El prefijo de Barcelona es "08", pero no se representa bien como Int
                codigoPostal = "0" + codigoPostal
            }
            codigoPostal
        }
    }

    private fun obtenerTelefono(data: JSONObject): String {
        // Algunos telefonos son string, otros son int y otros no existen
        return try {
            // Los numeros contienen espacios
            data.getString("telefon1").replace(" ", "")
        } catch (ex1: JSONException) {
            try {
                data.getInt("telefon1").toString()
            } catch (ex2: JSONException) {
                ""
            }
        }
    }

    private fun obtenerDescripcion(data: JSONObject): String {
        // Algunas descripciones son strings, otras son objetos y otras no existen
        return try {
            data.getString("propietats").duplicarApostrofos()
        } catch (ex: JSONException) {
            ""
        }
    }

    private fun obtenerCodigoLocalidad(data: JSONObject): String {
        // Algunos codigos de localidad son string y otros int
        return try {
            data.getString("codi_municipi")
        } catch (ex: JSONException) {
            data.getInt("codi_municipi").toString()
        }
    }

    private fun obtenerCodigoProvincia(codigoPostal: String): String {
        return codigoPostal.substring(0, 2)
    }

    private fun obtenerNombreProvincia(codigoProvincia: String): String {
        return when (codigoProvincia) {
            "08" -> "Barcelona"
            "17" -> "Girona"
            "25" -> "Lleida"
            "43" -> "Tarragona"
            else -> ""
        }
    }

}
package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.nio.file.Files
import java.nio.file.Paths


class ExtractorCV(jsonFile: String) : Extractor(jsonFile) {

    override fun extraerDatos() {
        val jsonString = String(Files.readAllBytes(Paths.get(jsonFile)))
        val jsonObject = JSONObject(JSONTokener(jsonString))
        val jsonArray = jsonObject.getJSONObject("response").getJSONArray("row")

        jsonArray.forEach {
            val element = it as JSONObject

            val nombreBiblioteca = element.getString("NOMBRE")
            val direccion = element.getString("DIRECCION")

            //**************SELENIUM*******************
            //    val longitud = element.getDouble("")
            //    val latitud = element.getDouble("")

            val email = element.getString("EMAIL")
            val nombreLocalidad = element.getString("NOM_MUNICIPIO")
            val codigoPostal = element.getString("CP")

            val telefono = element.getString("TELEFONO")
            val descripcion = element.getString("TIPO")
            val tipo = getTitularidad(element)
            val codigoLocalidad = element.getString("COD_MUNICIPIO")
            val codigoProvincia = element.getString("COD_PROVINCIA")
            val nombreProvincia = element.getString("NOM_PROVINCIA")

            val provincia = Provincia(nombreProvincia, codigoProvincia)
            val localidad = Localidad(nombreLocalidad, codigoLocalidad, provincia)
            val biblioteca = Biblioteca(
                nombreBiblioteca, tipo, direccion, codigoPostal, 0.0, 0.0,
                telefono, email, descripcion, localidad
            )

            dataWarehouse.addBiblioteca(biblioteca)
        }
    }
    private fun getTitularidad(data: JSONObject): Titularidad{
        var tipo = ""
        try{
            tipo = data.getString("DESC_CARACTER")
            return if(tipo.compareTo("PRIVADA") == 0 ){
                Titularidad.Privada
            }else Titularidad.Publica
        } catch (ex: JSONException){
            print("ERROR OBTENIENDO TITULARIDAD")
        }
       return Titularidad.Publica
        //ERROR Y PUBLICA POR TANTO
    }



}
package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.file.Files
import java.nio.file.Paths


class ExtractorCV(jsonFile: String) : Extractor(jsonFile) {

    override fun extraerDatos() {
        val jsonString = String(Files.readAllBytes(Paths.get(jsonFile)))
        val jsonArray = JSONArray(jsonString)

        jsonArray.forEach {
            val element = it as JSONObject

            val nombreBiblioteca = element.getString("NOMBRE").duplicarApostrofos()
            val direccion = element.getString("DIRECCION").duplicarApostrofos()

            //**************SELENIUM*******************
            //    val longitud = element.getDouble("")
            //    val latitud = element.getDouble("")

            val email = element.getString("EMAIL")
            val nombreLocalidad = element.getString("NOM_MUNICIPIO").duplicarApostrofos()
            val codigoPostal = element.getString("CP")

            val telefono = element.getString("TELEFONO")
            val descripcion = element.getString("TIPO")
            val tipo = getTitularidad(element)
            val codigoLocalidad = getCodLocalidad(element)
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
    private fun getCodLocalidad(data: JSONObject):String {
        var codLocalidad: String
        var localidad = data.getString("COD_MUNICIPIO")
        var codProv = data.getString("COD_PROVINCIA")

        var nomLocalidad = data.getString("NOM_MUNICIPIO")

        if(localidad.length <3){
            localidad = "0"+localidad;
        }
        codLocalidad = codProv+localidad

        if(nomLocalidad == "JESUS POBRE" ||nomLocalidad == "PUERTO SAGUNTO (EL)"
            || nomLocalidad == "MARENY DE BARRAQUETES" || nomLocalidad == "PERELLÓ (EL)"
            || nomLocalidad == "CAMPELL"
        ){
            codLocalidad += " " + nomLocalidad
        }

            return codLocalidad

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
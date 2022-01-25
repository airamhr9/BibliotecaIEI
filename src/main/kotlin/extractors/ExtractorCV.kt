package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import persistence.DataWarehouse
import persistence.FuenteDeDatos
import java.nio.file.Files
import java.nio.file.Paths

class ExtractorCV(jsonFile: String, val ubicacion: Ubicacion) : Extractor(jsonFile) {

    override fun extraerDatos() {
        val jsonString = String(Files.readAllBytes(Paths.get(jsonFile)))
        val jsonArray = JSONArray(jsonString)

        jsonArray.forEach {
            val element = it as JSONObject

            val nombreBiblioteca = element.getString("NOMBRE").duplicarApostrofos()
            val direccion = element.getString("DIRECCION").duplicarApostrofos()

            val email = element.getString("EMAIL")
            val nombreLocalidad = element.getString("NOM_MUNICIPIO").duplicarApostrofos()
            val codigoPostal = element.getString("CP")

            val telefono = getTelf(element)
            val descripcion = element.getString("TIPO")
            val tipo = getTitularidad(element)
            val codigoProvincia = element.getString("COD_PROVINCIA")
            val nombreProvincia = element.getString("NOM_PROVINCIA")

            val direccionCompleta = "$direccion, $codigoPostal $nombreLocalidad, España"
            val puntoGeografico = ubicacion.obtenerCoordenadas(direccionCompleta)

            val codigoLocalidad = generarIdentificadorDeLocalidad(codigosDeLocalidadCV, getCodLocalidad(element))

            val provincia = Provincia(nombreProvincia, codigoProvincia)
            val localidad = Localidad(nombreLocalidad, codigoLocalidad, provincia)
            val biblioteca = Biblioteca(
                nombreBiblioteca, tipo, direccion, codigoPostal, puntoGeografico.longitud, puntoGeografico.latitud,
                telefono, email, descripcion, localidad
            )

            DataWarehouse.addBiblioteca(biblioteca)
        }
        DataWarehouse.fuentesCargadas.add(FuenteDeDatos.ComunitatValenciana)
    }

    private fun getTelf(data: JSONObject):String{
        val telefono = data.getString("TELEFONO").removePrefix("TELF.")
        return telefono.take(9)
    }

    private fun getCodLocalidad(data: JSONObject):String {
        var codeL: String
        var codLocalidad = data.getString("COD_MUNICIPIO")
        val codProv = data.getString("COD_PROVINCIA")

        val nomLocalidad = data.getString("NOM_MUNICIPIO").duplicarApostrofos()

        if(codLocalidad.length <3){
            codLocalidad = "0"+codLocalidad
        }

        codeL = codProv + codLocalidad
        codeL += " " + nomLocalidad.take(15)

        return codeL
    }

    private fun getTitularidad(data: JSONObject): Titularidad{
        val tipo: String
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

    companion object {
        private val codigosDeLocalidadCV = mutableMapOf<String, String>()
    }

}
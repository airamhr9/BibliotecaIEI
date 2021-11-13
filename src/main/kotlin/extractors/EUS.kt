package extractors

import objects.Biblioteca
import objects.Localidad
import objects.Provincia
import objects.Titularidad
import java.io.Reader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class EUS {
    //val dataWarehouse: DataWarehouse = DataWarehouse()

    fun start() {
        val path : Path = Paths.get("src/bibliotecas.json")
        val reader : Reader = Files.newBufferedReader(path)

        val json = reader.readLines().toString()

        for( i in json.indices){
            if(json[i].equals('{')){
                for ( j in i until json.length ){
                    if(json[j] == '}'){
                        val biblioteca : String = json.substring(i,j)
                        extraer(biblioteca)
                        break
                    }
                }
            }
        }
    }

    private fun extraer(data : String){

        var ind1 = data.indexOf(("documentName")) + 17
        var ind2 = data.indexOf(("documentDescription")) - 7
        val nombre = data.substring(ind1, ind2)

        ind1 = data.indexOf(("address")) + 12
        ind2 = data.indexOf(("municipality")) - 7
        val direccion = data.substring(ind1, ind2)

        ind1 = data.indexOf(("postalcode")) + 15
        ind2 = data.indexOf(("territory")) - 7
        var codigoPostalS = data.substring(ind1, ind2)
        if(codigoPostalS[2].equals('.')){
            codigoPostalS = codigoPostalS.substring(0,2) + codigoPostalS.substring(3)
        }
        val locCodigo = codigoPostalS.substring(2)
        val proCod = codigoPostalS.substring(0,2)

        val codigoPostal = codigoPostalS

        ind1 = data.indexOf(("lonwgs84")) + 13
        ind2 = data.indexOf(("placename")) - 7
        val longitud = data.substring(ind1, ind2).toDouble()

        ind1 = data.indexOf(("latwgs84")) + 13
        ind2 = data.indexOf(("lonwgs84")) - 7
        val latitud = data.substring(ind1, ind2).toDouble()

        ind1 = data.indexOf(("phone")) + 10
        ind2 = data.indexOf(("email")) - 7
        val telefono = data.substring(ind1, ind2)

        ind1 = data.indexOf(("email")) + 10
        ind2 = data.indexOf(("webpage")) - 7
        val email = data.substring(ind1, ind2)

        ind1 = data.indexOf(("documentDescription")) + 24
        ind2 = data.indexOf(("libraryTimeTable")) - 7
        val descripcion = data.substring(ind1, ind2)

        ind1 = data.indexOf(("municipality")) + 17
        ind2 = data.indexOf(("municipalitycode")) - 7
        val locNombre = data.substring(ind1, ind2)

        ind1 = data.indexOf(("territory")) + 14
        ind2 = data.indexOf(("territorycode")) - 7
        val proNombre = data.substring(ind1, ind2)


        val provincia = Provincia(proNombre,proCod)
        val localidad = Localidad(locNombre,locCodigo,provincia)
        val biblioteca = Biblioteca(nombre, Titularidad.Publica, direccion, codigoPostal, longitud, latitud,
            telefono, email ,descripcion, localidad)

    }
}

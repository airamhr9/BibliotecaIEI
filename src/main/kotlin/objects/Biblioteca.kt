package objects

import java.sql.ResultSet

data class Biblioteca(val nombre : String,
                      val tipo : Titularidad,
                      val direccion : String,
                      val codigoPostal : String,
                      val longitud: Double,
                      val latitud : Double,
                      val telefono : String,
                      val email : String,
                      val descripcion : String,
                      val enLocalidad: Localidad) {
    companion object {
        fun fromResultSet(resultSet: ResultSet): Biblioteca {
            val provincia = Provincia(resultSet.getString("nombre_provincia"), resultSet.getString("codigo_provincia"))
            val localidad = Localidad(resultSet.getString("nombre_localidad"), resultSet.getString("codigo_localidad"), provincia)
            return Biblioteca(
                resultSet.getString("nombre_biblioteca"),
                if (resultSet.getString("tipo") == "publica") Titularidad.Publica else Titularidad.Privada,
                resultSet.getString("direccion"),
                resultSet.getString("codigopostal"),
                resultSet.getDouble("longitud"),
                resultSet.getDouble("latitud"),
                resultSet.getString("telefono"),
                resultSet.getString("email"),
                resultSet.getString("descripcion"),
                localidad)
        }
    }
}

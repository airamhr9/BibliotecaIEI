package persistence

import objects.Biblioteca
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import kotlin.system.exitProcess

class DataWarehouse private constructor(){
    private val connection : Connection

    companion object {
        private var dataWarehouse : DataWarehouse? = null

        fun getInstance() : DataWarehouse {
            if (dataWarehouse == null) {
                dataWarehouse = DataWarehouse()
            }
            return dataWarehouse!!
        }
    }

    init {
        try{
            val properties = Properties()

            //Populate the properties file with user name and password
            with(properties){
                put("user", "admin")
                put("password", "pw")
            }

            //Open a connection to the database
            connection = DriverManager.getConnection("jdbc:derby:biblioteca;create=true", properties)
            connection.autoCommit = false;
            println("Database opened")
        } catch (e : java.sql.SQLException) {
            System.err.println("Java SQL Exception\nCan't open database")
            exitProcess(-1)
        }
    }

    fun getBibliotecas() : ArrayList<Biblioteca> {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT *, biblioteca.nombre as nombre_biblioteca, localidad.codigo as codigo_localidad, provincia.codigo as codigo_provincia, localidad.nombre as nombre_localidad, provincia.nombre as nombre_provincia FROM biblioteca "+
                "LEFT JOIN localidad ON localidad.codigo = biblioteca.en_localidad  LEFT JOIN provincia ON provincia.codigo = localidad.en_provincia;")
        val result = ArrayList<Biblioteca>()
        while (resultSet.next()) {
            result.add(Biblioteca.fromResultSet(resultSet))
        }
        statement.close()
        resultSet.close()
        return result
    }

    fun addBiblioteca(biblioteca: Biblioteca) {
        val statement = connection.createStatement()
        println( "INSERT INTO provincia(codigo, nombre) VALUES (${biblioteca.enLocalidad.enProvincia.codigo}, \'${biblioteca.enLocalidad.enProvincia.nombre}\'); " +
                "INSERT INTO localidad(codigo, nombre, en_provincia) " +
                "VALUES (${biblioteca.enLocalidad.codigo}, \'${biblioteca.enLocalidad.nombre}\', ${biblioteca.enLocalidad.enProvincia.codigo}); " +
                "INSERT INTO biblioteca(nombre, tipo, direccion, codigopostal, longitud, latitud, telefono, email, descripcion, en_localidad) " +
                "VALUES (\'${biblioteca.nombre}\', \'${biblioteca.tipo}\', \'${biblioteca.direccion}\', ${biblioteca.codigoPostal}, " +
                "${biblioteca.longitud}, ${biblioteca.latitud}, ${biblioteca.telefono}, \'${biblioteca.email}\', \'${biblioteca.descripcion}\', " +
                "${biblioteca.enLocalidad.codigo});")
        statement.executeUpdate(
            "INSERT INTO provincia(codigo, nombre) VALUES (${biblioteca.enLocalidad.enProvincia.codigo}, \'${biblioteca.enLocalidad.enProvincia.nombre}\'); " +
                    "INSERT INTO localidad(codigo, nombre, en_provincia) " +
                    "VALUES (${biblioteca.enLocalidad.codigo}, \'${biblioteca.enLocalidad.nombre}\', ${biblioteca.enLocalidad.enProvincia.codigo}); " +
                    "INSERT INTO biblioteca(nombre, tipo, direccion, codigopostal, longitud, latitud, telefono, email, descripcion, en_localidad) " +
                    "VALUES (\'${biblioteca.nombre}\', \'${biblioteca.tipo}\', \'${biblioteca.direccion}\', ${biblioteca.codigoPostal}, " +
                    "${biblioteca.longitud}, ${biblioteca.latitud}, ${biblioteca.telefono}, \'${biblioteca.email}\', \'${biblioteca.descripcion}\', " +
                    "${biblioteca.enLocalidad.codigo});")
        connection.commit()
        statement.close()
    }
}
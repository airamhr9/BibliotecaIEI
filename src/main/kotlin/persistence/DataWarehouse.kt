package persistence

import objects.Biblioteca
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*
import kotlin.system.exitProcess

object DataWarehouse {

    private val connection: Connection
    val fuentesCargadas = mutableListOf<FuenteDeDatos>()

    init {
        try{
            val properties = Properties()

            //Populate the properties file with username and password
            with(properties){
                put("user", "admin")
                put("password", "pw")
            }

            //Open a connection to the database
            connection = DriverManager.getConnection("jdbc:derby:biblioteca;create=true", properties)
            connection.autoCommit = false
            val dbm: DatabaseMetaData = connection.metaData
            val rs: ResultSet = dbm.getTables(null, null, "PROVINCIA", null)
            val tableExists = rs.next()
            if (!tableExists) {
                createDatabase()
            }
            println("Database opened")
        } catch (e : java.sql.SQLException) {
            System.err.println("Java SQL Exception\nCan't open database")
            System.err.println(e.message)
            System.err.println(e.printStackTrace())
            exitProcess(-1)
        }
    }

    fun deleteTables() {
        val queries = listOf("DELETE FROM biblioteca", "DELETE FROM localidad", "DELETE FROM provincia")
        for (query in queries) {
            val statement = connection.createStatement()
            statement.executeUpdate(query)
            statement.close()
        }
        connection.commit()
        fuentesCargadas.clear()
        println("Tablas eliminadas")
    }


    private fun createDatabase() {
        val query = listOf("CREATE TABLE provincia (codigo VARCHAR(5) PRIMARY KEY, nombre VARCHAR(20))",
        "CREATE TABLE localidad (codigo VARCHAR(70) PRIMARY KEY, nombre VARCHAR (150),en_provincia VARCHAR(5),CONSTRAINT fk_provincia FOREIGN KEY(en_provincia) REFERENCES provincia(codigo))",
                "CREATE TABLE biblioteca (nombre VARCHAR(200),tipo VARCHAR(20), direccion VARCHAR(100),codigoPostal VARCHAR(10), longitud float, " +
                "latitud float,telefono VARCHAR(45),email VARCHAR(250)," +
                "descripcion VARCHAR(200), en_localidad VARCHAR(70), FOREIGN KEY(en_localidad) REFERENCES localidad(codigo))")
        for (createStatement in query){
            println(createStatement)
            val statement = connection.createStatement()
            statement.executeUpdate(createStatement)
            statement.close()
        }
        connection.commit()
        println("Tablas creadas")
    }

    fun getBibliotecas() : List<Biblioteca> {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(
                "SELECT biblioteca.nombre as nombre_biblioteca, tipo, direccion, telefono, codigoPostal, latitud, longitud, email, descripcion, \n" +
                "localidad.codigo as codigo_localidad, " +
                "provincia.codigo as codigo_provincia, localidad.nombre as nombre_localidad, " +
                "provincia.nombre as nombre_provincia " +
                "FROM biblioteca "+
                "LEFT JOIN localidad ON localidad.codigo = biblioteca.en_localidad  LEFT JOIN provincia ON provincia.codigo = localidad.en_provincia")
        val result = mutableListOf<Biblioteca>()
        while (resultSet.next()) {
            result.add(Biblioteca.fromResultSet(resultSet))
        }
        statement.close()
        resultSet.close()
        return result
    }

    fun addBiblioteca(biblioteca: Biblioteca) {
        val codProv = biblioteca.enLocalidad.enProvincia.codigo
        val nomProv = biblioteca.enLocalidad.enProvincia.nombre
        val codLoc = biblioteca.enLocalidad.codigo
        val nomLoc = biblioteca.enLocalidad.nombre
        val query = listOf(
            "INSERT INTO provincia (  \n" +
                    "  SELECT \'$codProv\' as codigo, \'$nomProv\' as nombre\n" +
                    "  FROM provincia  \n" +
                    "  WHERE  \n" +
                    "    codigo = \'$codProv\' AND nombre = \'$nomProv\'  \n" +
                    "  HAVING count(*)=0  \n" +
                    " )",
            "INSERT INTO localidad (  \n" +
                    "  SELECT \'$codLoc\' as codigo, \'$nomLoc\' as nombre, \'$codProv\' as en_provincia" +
                    "  FROM localidad  \n" +
                    "  WHERE  \n" +
                    "    codigo = \'$codLoc\' AND nombre = \'$nomLoc\' AND en_provincia = \'$codProv\' \n" +
                    "  HAVING count(*)=0  \n" +
                    " )",
                    "INSERT INTO biblioteca(nombre, tipo, direccion, codigopostal, longitud, latitud, telefono, email, descripcion, en_localidad) " +
                    "VALUES (\'${biblioteca.nombre}\', \'${biblioteca.tipo}\', \'${biblioteca.direccion}\', \'${biblioteca.codigoPostal}\', " +
                    "${biblioteca.longitud}, ${biblioteca.latitud}, '${biblioteca.telefono}', \'${biblioteca.email}\', \'${biblioteca.descripcion}\', " +
                    "\'${biblioteca.enLocalidad.codigo}\')"
        )
        for (createStatement in query){
            println(createStatement)
            val statement = connection.createStatement()
            statement.executeUpdate(createStatement)
            statement.close()
        }
        connection.commit()
    }
}
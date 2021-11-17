package persistence

import objects.Biblioteca
import oracle.net.aso.c
import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.DriverManager
import java.sql.ResultSet
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

    fun createDatabase() {
        val query = listOf("CREATE TABLE provincia (codigo INT PRIMARY KEY, nombre VARCHAR(20))",
        "CREATE TABLE localidad (codigo INT PRIMARY KEY, nombre VARCHAR (70),en_provincia int,CONSTRAINT fk_provincia FOREIGN KEY(en_provincia) REFERENCES provincia(codigo))",
                "CREATE TABLE biblioteca (nombre VARCHAR(200),tipo VARCHAR(10), direccion VARCHAR(100),codigoPostal int, longitud float, " +
                "latitud float,telefono VARCHAR(30),email VARCHAR(60)," +
                "descripcion VARCHAR(100),en_localidad int, FOREIGN KEY(en_localidad) REFERENCES localidad(codigo))")
        for (createStatement in query){
            println(createStatement)
            val statement = connection.createStatement()
            statement.executeUpdate(createStatement)
            statement.close()
        }
        connection.commit()
        println("Tablas creadas")
    }

    fun getBibliotecas() : ArrayList<Biblioteca> {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(
                "SELECT biblioteca.nombre as nombre_biblioteca, tipo, direccion, telefono, codigoPostal, latitud, longitud, email, descripcion, \n" +
                "localidad.codigo as codigo_localidad, " +
                "provincia.codigo as codigo_provincia, localidad.nombre as nombre_localidad, " +
                "provincia.nombre as nombre_provincia " +
                "FROM biblioteca "+
                "LEFT JOIN localidad ON localidad.codigo = biblioteca.en_localidad  LEFT JOIN provincia ON provincia.codigo = localidad.en_provincia")
        val result = ArrayList<Biblioteca>()
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
                    "  SELECT $codProv as codigo, \'$nomProv\' as nombre\n" +
                    "  FROM provincia  \n" +
                    "  WHERE  \n" +
                    "    codigo = $codProv AND nombre = \'$nomProv\'  \n" +
                    "  HAVING count(*)=0  \n" +
                    " )",
            "INSERT INTO localidad (  \n" +
                    "  SELECT $codLoc as codigo, \'$nomLoc\' as nombre, $codProv as en_provincia" +
                    "  FROM localidad  \n" +
                    "  WHERE  \n" +
                    "    codigo = $codLoc AND nombre = \'$nomLoc\' AND en_provincia = $codProv \n" +
                    "  HAVING count(*)=0  \n" +
                    " )",
                    "INSERT INTO biblioteca(nombre, tipo, direccion, codigopostal, longitud, latitud, telefono, email, descripcion, en_localidad) " +
                    "VALUES (\'${biblioteca.nombre}\', \'${biblioteca.tipo}\', \'${biblioteca.direccion}\', ${biblioteca.codigoPostal}, " +
                    "${biblioteca.longitud}, ${biblioteca.latitud}, '${biblioteca.telefono}', \'${biblioteca.email}\', \'${biblioteca.descripcion}\', " +
                    "${biblioteca.enLocalidad.codigo})"
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
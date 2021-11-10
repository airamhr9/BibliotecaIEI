package persistence

import java.sql.Connection
import java.sql.DriverManager
import kotlin.system.exitProcess

class DataWarehouse private constructor(){
    private val connection : Connection
    private val databaseURL = "jdbc:postgresql://172.17.0.2:5432/almaceniei" // Airam
    // private val databaseURL = "jdbc:postgresql://localhost:5432/almaceniei"

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
            Class.forName("org.postgresql.Driver")
            connection = DriverManager.getConnection(databaseURL, "postgres", "ieibiblioteca")
            connection.autoCommit = false;
            println("Database opened")
        } catch (e : java.sql.SQLException) {
            System.err.println("Java SQL Exception\nCan't open database")
            exitProcess(-1)
        }
    }
}
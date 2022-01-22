package server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import persistence.DataWarehouse

@SpringBootApplication
class Server {
    companion object {
        lateinit var jsonFileEUS: String
        lateinit var xmlFileCAT: String
        lateinit var jsonFileCAT: String
        lateinit var csvFileCV: String
        lateinit var jsonFileCV: String
        lateinit var chromeDriverPath: String
    }
}

fun main(args: Array<String>) {
    leerPaths(args)
    DataWarehouse.deleteTables()

    runApplication<Server>(*args)
}

private fun leerPaths(args: Array<String>) {
    Server.jsonFileEUS = args[0]
    Server.xmlFileCAT = args[1]
    Server.jsonFileCAT = args[2]
    Server.csvFileCV = args[3]
    Server.jsonFileCV = args[4]
    Server.chromeDriverPath = args[5]

    println("Fuente de datos de Euskadi: ${Server.jsonFileEUS}")
    println("Fuente de datos de Cataluña: ${Server.xmlFileCAT} ${Server.jsonFileCAT}")
    println("Fuente de datos de la Comunidad Valenciana: ${Server.csvFileCV} ${Server.jsonFileCV}")
    println("Chrome driver: ${Server.chromeDriverPath}\n")
}
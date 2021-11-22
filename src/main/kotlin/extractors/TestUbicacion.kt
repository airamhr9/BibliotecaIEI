package extractors

fun main() {
    val driverPath = "C:\\Users\\hecto\\Desktop\\chromedriver.exe"
    val ubicacion = Ubicacion(driverPath)

    // A veces falla y devuelve coordenadas random xd (especialmente en la longitud)
    val direccion1 = "Carrer de les Glories Valencianes, 4, 46133 Meliana, España"
    val punto1 = ubicacion.obtenerCoordenadas(direccion1)
    println(punto1)

    // Con más de una dirección no funciona
    /* val direccion2 = "Passatge d'Enric Valor i Vives, 46022 Valencia, España"
    val punto2 = ubicacion.obtenerCoordenadas(direccion2)
    println(punto2) */

    // ubicacion.cerrarNavegador()
}
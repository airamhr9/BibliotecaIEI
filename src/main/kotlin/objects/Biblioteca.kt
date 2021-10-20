package objects

data class Biblioteca(val nombre : String,
                      val tipo : String,
                      val direccion : String,
                      val codigoPostal : Int,
                      val longitud: Double,
                      val latitud : Double,
                      val telefono : Int,
                      val email : String,
                      val descripcion : String,
                      val enLocalidad: Localidad)

package objects

data class Biblioteca(val nombre : String,
                      val tipo : Titularidad,
                      val direccion : String,
                      val codigoPostal : String,
                      val longitud: Double,
                      val latitud : Double,
                      val telefono : String,
                      val email : String,
                      val descripcion : String,
                      val enLocalidad: Localidad)

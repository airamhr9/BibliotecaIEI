package objects

data class Localidad(val nombre: String, val codigo: String, val enProvincia: Provincia) {

    override fun equals(other: Any?): Boolean {
        return other is Localidad
                && this.nombre == other.nombre
                && this.enProvincia == other.enProvincia
    }

    override fun hashCode(): Int {
        var result = nombre.hashCode()
        result = 31 * result + enProvincia.hashCode()
        return result
    }

}
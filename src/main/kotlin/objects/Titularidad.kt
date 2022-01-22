package objects

enum class Titularidad(val value: String) {
    Publica("Publica"),
    Privada("Privada");

    companion object {
        fun fromString(string: String) = values().first { it.value.equals(string, ignoreCase = true) }
    }

}
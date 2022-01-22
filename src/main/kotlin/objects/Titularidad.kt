package objects

enum class Titularidad(val value: String) {
    Publica("publica"),
    Privada("privada");

    companion object {
        fun fromString(value: String) = values().first { it.value == value }
    }

}
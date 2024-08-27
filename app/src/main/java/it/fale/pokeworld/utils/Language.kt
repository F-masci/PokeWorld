package it.fale.pokeworld.utils

/**
 * Enumerazione per le lingue supportate dall'applicazione.
 */
enum class Language(val text: String, val code: String) {

    ENGLISH("English", "en"),
    ITALIAN("Italiano", "it");

    companion object {
        fun fromText(text: String): Language {
            return Language.entries.find { it.text == text } ?: ENGLISH
        }

        fun fromCode(code: String): Language {
            return Language.entries.find { it.code == code } ?: ENGLISH
        }
    }

}
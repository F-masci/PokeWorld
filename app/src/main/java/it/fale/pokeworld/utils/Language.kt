package it.fale.pokeworld.utils

import it.fale.pokeworld.entity.PokemonType
import kotlin.random.Random

enum class Language(val text: String, val code: String) {

    ENGLISH("English", "en"),
    ITALIAN("Italiano", "it");

    companion object {
        fun fromText(text: String): Language {
            return Language.entries.find { it.text == text } ?: Language.ENGLISH
        }

        fun fromCode(code: String): Language {
            return Language.entries.find { it.code == code } ?: Language.ENGLISH
        }
    }

}
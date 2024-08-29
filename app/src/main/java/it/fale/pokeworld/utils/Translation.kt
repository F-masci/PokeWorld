package it.fale.pokeworld.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Classe per contenere la traduzione di un testo specifico
 *
 * @param language La lingua del testo
 * @param text Il testo in quella lingua
 */
data class Translation(val language: String, val text: String)

/**
 * Classe per la conversione tra una lista di oggetti Translation e una stringa JSON.
 */
class TranslationListConverter {

    /**
     * Converte una lista di oggetti Translation in una stringa JSON.
     *
     * @param translations La lista di oggetti Translation da convertire.
     * @return Una stringa JSON che rappresenta la lista di oggetti Translation.
     */
    @TypeConverter
    fun fromTranslationList(translations: List<Translation>): String {
        val gson = Gson()
        return gson.toJson(translations)
    }

    /**
     * Converte una stringa JSON in una lista di oggetti Translation.
     *
     * @param data La stringa JSON da convertire.
     * @return Una lista di oggetti Translation che rappresenta la stringa JSON.
     */
    @TypeConverter
    fun toTranslationList(data: String): List<Translation> {
        val listType = object : TypeToken<List<Translation>>() {}.type
        return Gson().fromJson(data, listType)
    }

}
package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.fale.pokeworld.repository.UserPreferencesRepository
import it.fale.pokeworld.utils.Translation
import it.fale.pokeworld.utils.TranslationListConverter

/**
 * Classe che rappresenta un oggetto di gioco
 */
@Entity(tableName = "item")
@TypeConverters(TranslationListConverter::class)
data class ItemEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val cost: Int,
    val names: List<Translation> = emptyList(),
    val effects: List<Translation> = emptyList(),
    val sprite: String?
) {

    /**
     * Ottiene il nome dell'oggetto in base alla lingua dell'utente
     *
     * @return Nome dell'oggetto in base alla lingua dell'utente
     */
    fun getLocaleName(): String {
        val language = UserPreferencesRepository.getLanguagePreference()
        val name = names.find { it.language == language.code }
        return name?.text ?: "Translation not available"
    }

    /**
     * Ottiene l'effetto dell'oggetto in base alla lingua dell'utente
     *
     * @return Effetto dell'oggetto in base alla lingua dell'utente
     */
    fun getLocaleEffect(): String {
        val language = UserPreferencesRepository.getLanguagePreference()
        val effect = effects.find { it.language == language.code }
        return effect?.text ?: "Translation not available"
    }
}
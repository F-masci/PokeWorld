package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.fale.pokeworld.repository.UserPreferencesRepository
import it.fale.pokeworld.utils.Translation
import it.fale.pokeworld.utils.TranslationListConverter

/**
 * Classe che rappresenta una abilità di un Pokemon
 */
@Entity(tableName = "ability")
@TypeConverters(TranslationListConverter::class)
data class AbilityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isMainSeries: Boolean,
    val names: List<Translation> = emptyList(),
    val effects: List<Translation> = emptyList()
) {

    /**
     * Ottiene il nome dell'abilità in base alla lingua dell'utente
     *
     * @return Nome dell'abilità in base alla lingua dell'utente
     */
    fun getLocaleName(): String {
        val language = UserPreferencesRepository.getLanguagePreference()
        val name = names.find { it.language == language.code }
        return name?.text ?: "Translation not available"
    }

    /**
     * Ottiene la descrizione dell'abilità in base alla lingua dell'utente
     *
     * @return Descrizione dell'abilità in base alla lingua dell'utente
     */
    fun getLocaleEffect(): String {
        val language = UserPreferencesRepository.getLanguagePreference()
        val effect = effects.find { it.language == language.code }
        return effect?.text ?: "Translation not available"
    }
}
package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.fale.pokeworld.repository.UserPreferencesRepository
import it.fale.pokeworld.utils.Translation
import it.fale.pokeworld.utils.TranslationListConverter

/**
 * Classe che rappresenta una mossa di un Pokemon
 */
@Entity(tableName = "move")
@TypeConverters(TranslationListConverter::class)
data class MoveEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val accuracy: Int?,
    val effectChance: Int?,
    val power: Int?,
    val pp: Int?,
    val priority: Int?,
    val type: PokemonType,
    val names: List<Translation> = emptyList(),
    val descriptions: List<Translation> = emptyList(),
) {

    /**
     * Ottiene il nome della mossa in base alla lingua dell'utente
     *
     * @return Nome della mossa in base alla lingua dell'utente
     */
    fun getLocaleName(): String {
        val language = UserPreferencesRepository.getLanguagePreference()
        val name = names.find { it.language == language.code }
        return name?.text ?: "Translation not available"
    }

    /**
     * Ottiene la descrizione della mossa in base alla lingua dell'utente
     *
     * @return Descrizione della mossa in base alla lingua dell'utente
     */
    fun getLocaleDescription(): String {
        val language = UserPreferencesRepository.getLanguagePreference()
        val description = descriptions.find { it.language == language.code }
        return description?.text ?: "Translation not available"
    }
}
package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.fale.pokeworld.utils.Translation
import it.fale.pokeworld.utils.TranslationListConverter
import java.util.Locale

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

    fun getLocaleName(): String {
        val name = names.find { it.language == Locale.getDefault().language }
        return name?.text ?: ""
    }

    fun getLocaleDescription(): String {
        val description = descriptions.find { it.language == Locale.getDefault().language }
        return description?.text ?: ""
    }
}
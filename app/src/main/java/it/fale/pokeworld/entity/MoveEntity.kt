package it.fale.pokeworld.entity

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.fale.pokeworld.utils.LANGUAGE_KEY
import it.fale.pokeworld.utils.PREFERENCES_NAME
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

    fun getLocaleName(context: Context): String {
        val language = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(LANGUAGE_KEY, Locale.getDefault().language)!!
        val name = names.find { it.language == language }
        return name?.text ?: ""
    }

    fun getLocaleDescription(context: Context): String {
        val language = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(LANGUAGE_KEY, Locale.getDefault().language)!!
        val description = descriptions.find { it.language == language }
        return description?.text ?: ""
    }
}
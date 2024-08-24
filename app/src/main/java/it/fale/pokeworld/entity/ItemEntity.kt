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

    fun getLocaleName(context: Context): String {
        val language = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(
            LANGUAGE_KEY, Locale.getDefault().language)!!
        val name = names.find { it.language == language }
        return name?.text ?: ""
    }

    fun getLocaleEffect(context: Context): String {
        val language = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getString(LANGUAGE_KEY, Locale.getDefault().language)!!
        val effect = effects.find { it.language == language }
        return effect?.text ?: ""
    }
}
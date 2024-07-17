package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
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

    fun getLocaleName(): String {
        val name = names.find { it.language == Locale.getDefault().language }
        return name?.text ?: ""
    }

    fun getLocaleEffect(): String {
        val effect = effects.find { it.language == Locale.getDefault().language }
        return effect?.text ?: ""
    }
}
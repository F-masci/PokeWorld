package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.fale.pokeworld.utils.Translation
import it.fale.pokeworld.utils.TranslationListConverter
import java.util.Locale

@Entity(tableName = "ability")
@TypeConverters(TranslationListConverter::class)
data class AbilityEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val isMainSeries: Boolean,
    val names: List<Translation> = emptyList(),
    val effects: List<Translation> = emptyList()
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
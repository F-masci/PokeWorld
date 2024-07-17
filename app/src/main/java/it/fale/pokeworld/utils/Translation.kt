package it.fale.pokeworld.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Translation(val language: String, val text: String)

class TranslationListConverter {

    @TypeConverter
    fun fromTranslationList(translations: List<Translation>): String {
        val gson = Gson()
        return gson.toJson(translations)
    }

    @TypeConverter
    fun toTranslationList(data: String):List<Translation> {
        val listType = object : TypeToken<List<Translation>>() {}.type
        return Gson().fromJson(data, listType)
    }

}
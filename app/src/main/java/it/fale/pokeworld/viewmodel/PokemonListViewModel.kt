package it.fale.pokeworld.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.entity.repository.PokemonRepository
import it.fale.pokeworld.utils.LANGUAGE_KEY
import it.fale.pokeworld.utils.Language
import it.fale.pokeworld.utils.PREFERENCES_NAME
import it.fale.pokeworld.utils.THEME_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class PokemonListViewModel(private val repository: PokemonRepository): ViewModel() {

    private var _pokemonList: List<PokemonEntity> = emptyList()
    private val _filteredPokemonList: MutableStateFlow<List<PokemonEntity>> = MutableStateFlow(emptyList())

    val pokemonList: StateFlow<List<PokemonEntity>>
        get() = _filteredPokemonList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList = repository.retrievePokemonList()
            _filteredPokemonList.value = _pokemonList
        }
    }

    fun filterPokemon(name: String?, type1: PokemonType?, type2: PokemonType?) {
        var filteredList = _pokemonList

        if (name?.isNotBlank() == true) filteredList = filteredList.filter { it.name.contains(name, ignoreCase = true) }
        if (type1 != null) filteredList = filteredList.filter { it.type1 == type1 }
        if (type2 != null) filteredList = filteredList.filter { it.type2 == type2 }

        _filteredPokemonList.value = filteredList
    }

    fun randomFilters(): Pair<PokemonType, PokemonType> {
        var randomPokemonType1: PokemonType
        var randomPokemonType2: PokemonType
        var filteredList: List<PokemonEntity>

        do {
            randomPokemonType1 = PokemonType.getRandomPokemonType()
            randomPokemonType2 = PokemonType.getRandomPokemonType()
            filteredList = _pokemonList.filter { it.type1 == randomPokemonType1 && it.type2 == randomPokemonType2 }
        } while (filteredList.isEmpty())

        return Pair(randomPokemonType1, randomPokemonType2)
    }

    // Gestione del tema
    fun saveThemePreference(context: Context, isDarkTheme: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(THEME_KEY, isDarkTheme).apply()
    }

    fun getThemePreference(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    //Gestione della lingua
    fun saveLanguagePreference(context: Context, language: Language) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LANGUAGE_KEY, language.code).apply()
    }

    fun getLanguagePreference(context: Context): Language {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return Language.fromCode(sharedPreferences.getString(LANGUAGE_KEY, Locale.getDefault().language)!!)
    }

}
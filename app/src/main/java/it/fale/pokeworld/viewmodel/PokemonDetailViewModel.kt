package it.fale.pokeworld.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(private val repository: PokemonRepository): ViewModel() {

    private val _pokemon: MutableStateFlow<PokemonEntity> = MutableStateFlow(PokemonEntity(id = 0, name = "Loading..."))
    private val _detailsLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val pokemon: StateFlow<PokemonEntity>
        get() = _pokemon.asStateFlow()

    val detailsLoaded: StateFlow<Boolean>
        get() = _detailsLoaded.asStateFlow()

    fun loadPokemon(pokemonId: Int) {
        _detailsLoaded.value = false
        viewModelScope.launch(Dispatchers.IO) {
            _pokemon.value = repository.retrievePokemon(pokemonId, PokemonRepository.LOAD_ALL)
            _detailsLoaded.value = true
        }
    }
    fun saveFavoritePokemon(context: Context, pokemonId: Int) {
        val sharedPreferences = context.getSharedPreferences("pokemon_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("favorite_pokemon_id", pokemonId)
        editor.apply()
    }

    fun getFavoritePokemonId(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences("pokemon_prefs", Context.MODE_PRIVATE)
        val favoriteId = sharedPreferences.getInt("favorite_pokemon_id", -1)
        return if (favoriteId == -1) null else favoriteId
    }

    fun clearFavoritePokemon(context: Context) {
        val sharedPreferences = context.getSharedPreferences("pokemon_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("favorite_pokemon_id")
        editor.apply()
    }

}
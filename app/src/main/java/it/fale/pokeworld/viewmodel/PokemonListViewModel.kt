package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(private val repository: PokemonRepository): ViewModel() {

    private val _pokemonList: MutableStateFlow<List<PokemonEntity>> = MutableStateFlow(emptyList())

    val pokemonList: StateFlow<List<PokemonEntity>>
        get() = _pokemonList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList.value = repository.retrievePokemonList()
            // repository.insertPokemon()
        }
    }
}
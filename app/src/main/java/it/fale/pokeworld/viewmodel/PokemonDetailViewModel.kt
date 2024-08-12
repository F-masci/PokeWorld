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

class PokemonDetailViewModel(private val repository: PokemonRepository, private val pokemonId: Int): ViewModel() {

    private val _pokemon: MutableStateFlow<PokemonEntity> = MutableStateFlow(PokemonEntity(id = 0, name = "Loading..."))
    private val _detailsLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val pokemon: StateFlow<PokemonEntity>
        get() = _pokemon.asStateFlow()

    val detailsLoaded: StateFlow<Boolean>
        get() = _detailsLoaded.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemon.value = repository.retrievePokemon(pokemonId, PokemonRepository.LOAD_ALL)
            _detailsLoaded.value = true
        }
    }

}
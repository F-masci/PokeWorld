package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fale.pokeworld.repository.PokemonRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val pokemonRepository: PokemonRepository,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java))
            return PokemonListViewModel(pokemonRepository) as T
        else if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java))
            return PokemonDetailViewModel(pokemonRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fale.pokeworld.entity.repository.PokemonRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: PokemonRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) return PokemonListViewModel(repository) as T
        else if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java)) return PokemonDetailViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
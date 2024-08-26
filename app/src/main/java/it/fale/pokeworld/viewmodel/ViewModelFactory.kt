package it.fale.pokeworld.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fale.pokeworld.entity.repository.PokemonRepository
import it.fale.pokeworld.viewmodel.shared.FavoritePokemonSharedRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: PokemonRepository, private val favoritePokemonSharedRepository: FavoritePokemonSharedRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java)) return PokemonListViewModel(repository, favoritePokemonSharedRepository) as T
        else if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java)) return PokemonDetailViewModel(repository, favoritePokemonSharedRepository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
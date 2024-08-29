package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fale.pokeworld.repository.PokemonRepository

/**
 * Factory per la creazione di ViewModel.
 *
 * @param pokemonRepository Il repository per le operazioni di database.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val pokemonRepository: PokemonRepository,
): ViewModelProvider.Factory {

    /**
     * Crea una nuova istanza di ViewModel.
     *
     * @param modelClass La classe del ViewModel da creare.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PokemonListViewModel::class.java))
            return PokemonListViewModel(pokemonRepository) as T
        else if (modelClass.isAssignableFrom(PokemonDetailViewModel::class.java))
            return PokemonDetailViewModel(pokemonRepository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
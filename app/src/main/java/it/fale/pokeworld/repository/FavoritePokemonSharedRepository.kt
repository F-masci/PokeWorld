package it.fale.pokeworld.repository

import it.fale.pokeworld.entity.PokemonEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository per la gestione del pokemon preferito
 */
object FavoritePokemonSharedRepository {

    // Variabile per memorizzare il pokemon preferito
    private val _sharedFavoritePokemon = MutableStateFlow<PokemonEntity?>(null)
    val sharedFavoritePokemon: StateFlow<PokemonEntity?>
        get() = _sharedFavoritePokemon.asStateFlow()

    /**
     * Aggiorna il pokemon preferito.
     *
     * @param pokemon Il nuovo pokemon preferito
     */
    fun updateFavoritePokemon(pokemon: PokemonEntity?) {
        _sharedFavoritePokemon.value = pokemon
    }

}
package it.fale.pokeworld.viewmodel.shared

import it.fale.pokeworld.entity.PokemonEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritePokemonSharedRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val _sharedFavoritePokemon = MutableStateFlow<PokemonEntity?>(null)
    val sharedFavoritePokemon: StateFlow<PokemonEntity?>
        get() = _sharedFavoritePokemon.asStateFlow()

    fun updateFavoritePokemon(pokemon: PokemonEntity?) {
        _sharedFavoritePokemon.value = pokemon
    }

}
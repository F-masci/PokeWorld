package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.repository.PokemonRepository
import it.fale.pokeworld.repository.FavoritePokemonSharedRepository
import it.fale.pokeworld.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel per la schermata dei dettagli di un pokemon.
 *
 *  @param pokemonRepository Repository per le operazioni di database.
 */
class PokemonDetailViewModel(
    private val pokemonRepository: PokemonRepository,
): ViewModel() {

    // Pokemon da visualizzare
    private val _pokemon: MutableStateFlow<PokemonEntity?> = MutableStateFlow(null)
    private val _detailsLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val pokemon: StateFlow<PokemonEntity?>
        get() = _pokemon.asStateFlow()

    val detailsLoaded: StateFlow<Boolean>
        get() = _detailsLoaded.asStateFlow()

    /**
     * Carica i dettagli di un pokemon.
     *
     * @param pokemonId Id del pokemon da caricare.
     */
    fun loadPokemon(pokemonId: Int) {
        // Specifica che i valori non sono ancora stati caricati
        _detailsLoaded.value = false
        viewModelScope.launch(Dispatchers.IO) {
            // Carica i dettagli del pokemon dal DB
            _pokemon.value = pokemonRepository.retrievePokemon(pokemonId, PokemonRepository.LOAD_ALL)
            // Specifica che i valori sono ancora stati caricati
            _detailsLoaded.value = true
        }
    }

    /**
     * Salva il pokemon preferito dell'utente.
     * Il pokemonId viene preso dal pokemon correntemente visualizzato.
     */
    fun saveFavoritePokemon() {
        UserPreferencesRepository.saveFavoritePokemonId(pokemon.value!!.id)
        FavoritePokemonSharedRepository.updateFavoritePokemon(pokemon.value)
    }

    /**
     * Ritorna l'id del pokemon preferito dell'utente.
     *
     * @return Id del pokemon preferito dell'utente.
     */
    fun getFavoritePokemonId(): Int? {
        return UserPreferencesRepository.getFavoritePokemonId()
    }

    /**
     * Rimuove il pokemon preferito dell'utente.
     */
    fun clearFavoritePokemon() {
        UserPreferencesRepository.clearFavoritePokemonId()
        FavoritePokemonSharedRepository.updateFavoritePokemon(null)
    }

}
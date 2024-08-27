package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.repository.PokemonRepository
import it.fale.pokeworld.repository.FavoritePokemonSharedRepository
import it.fale.pokeworld.repository.UserPreferencesRepository
import it.fale.pokeworld.utils.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * Classe ViewModel per la schermata PokemonListScreen.
 *
 * @param pokemonRepository Repository per le operazioni di database.
 */
class PokemonListViewModel(
    private val pokemonRepository: PokemonRepository,
): ViewModel() {

    // Lista dei pokemon da visualizzare sulla schermata principale
    private var _pokemonList: List<PokemonEntity> = emptyList()
    private val _filteredPokemonList: MutableStateFlow<List<PokemonEntity>> = MutableStateFlow(emptyList())

    // Pokemon preferito selezionato dall'utente
    val favoritePokemon: StateFlow<PokemonEntity?> = FavoritePokemonSharedRepository.sharedFavoritePokemon
    val pokemonList: StateFlow<List<PokemonEntity>>
        get() = _filteredPokemonList.asStateFlow()

    // Inizializzazione della lista dei pokemon
    init {
        viewModelScope.launch(Dispatchers.IO) {
            // Carica la lista dei pokemon dal db
            _pokemonList = pokemonRepository.retrievePokemonList()
            _filteredPokemonList.value = _pokemonList
        }
    }

    /**
     * Filtra la lista dei pokemon in base al nome e ai due tipi selezionati.
     * Se viene passato come parametro null la funzione non considera quel filtro.
     *
     * @param name Nome del pokemon da cercare.
     * @param type1 Tipo 1 del pokemon da cercare.
     * @param type2 Tipo 2 del pokemon da cercare.
     */
    fun filterPokemon(name: String?, type1: PokemonType?, type2: PokemonType?) {
        // Contiene la lista da filtrare
        var filteredList = _pokemonList

        // Filtra per nome
        if (name?.isNotBlank() == true) filteredList = filteredList.filter { it.name.contains(name, ignoreCase = true) }
        // Filtra per il tipo 1
        if (type1 != null) filteredList = filteredList.filter { it.type1 == type1 }
        // Filtra per il tipo 2
        if (type2 != null) filteredList = filteredList.filter { it.type2 == type2 }

        // Risultato del filtro
        _filteredPokemonList.value = filteredList
    }

    /**
     * Genera casualmente due tipi di pokemon diversi.
     * La funzione controlla che i filtri non producano in uscita una lista vuota.
     *
     * @return Una coppia di tipi di pokemon casuali.
     */
    fun randomFilters(): Pair<PokemonType, PokemonType> {

        // Variabili per memorizzare i tipi di pokemon casuali
        var randomPokemonType1: PokemonType
        var randomPokemonType2: PokemonType

        // Lista dei pokemon da filtrare per controllare non sia vuota
        var filteredList: List<PokemonEntity>

        // Controllo che i filtri non producano una lista vuota di pokemon
        do {
            randomPokemonType1 = PokemonType.getRandomPokemonType()
            randomPokemonType2 = PokemonType.getRandomPokemonType()
            filteredList = _pokemonList.filter { it.type1 == randomPokemonType1 && it.type2 == randomPokemonType2 }
        } while (filteredList.isEmpty())

        return Pair(randomPokemonType1, randomPokemonType2)
    }

    /**
     * Aggiorna il tema selezionato dall'utente.
     *
     * @param isDarkTheme Valore booleano che indica se il tema è scuro.
     */
    fun saveDarkModePreference(isDarkTheme: Boolean) {
        UserPreferencesRepository.saveDarkModePreference(isDarkTheme)
    }

    /**
     * Aggiorna la lingua selezionata dall'utente.
     *
     * @param language Lingua selezionata dall'utente.
     */
    fun saveLanguagePreference(language: Language) {
        UserPreferencesRepository.saveLanguagePreference(language)
    }

}
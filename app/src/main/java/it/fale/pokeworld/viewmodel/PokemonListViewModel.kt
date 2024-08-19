package it.fale.pokeworld.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.entity.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class PokemonListViewModel(private val repository: PokemonRepository): ViewModel() {

    private var _pokemonList: List<PokemonEntity> = emptyList();
    private val _filteredPokemonList: MutableStateFlow<List<PokemonEntity>> = MutableStateFlow(emptyList())

    val pokemonList: StateFlow<List<PokemonEntity>>
        get() = _filteredPokemonList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonList = repository.retrievePokemonList()
            _filteredPokemonList.value = _pokemonList
        }
    }

    fun filterPokemon(name: String?, type1: PokemonType?, type2: PokemonType?) {

        var filteredList = _pokemonList

        if(name?.isBlank() == false) filteredList = filteredList.filter { it.name.contains(name, ignoreCase = true) }
        if(type1 != null) filteredList = filteredList.filter { it.type1 == type1 }
        if(type2 != null) filteredList = filteredList.filter { it.type2 == type2 }

        _filteredPokemonList.value = filteredList
    }

    fun isThereAnyPokemon(name: String?, type1: PokemonType?, type2: PokemonType?): Boolean {

        var filteredList = _pokemonList

        if(name?.isBlank() == false) filteredList = filteredList.filter { it.name.contains(name, ignoreCase = true) }
        if(type1 != null) filteredList = filteredList.filter { it.type1 == type1 }
        if(type2 != null) filteredList = filteredList.filter { it.type2 == type2 }

        return filteredList.isEmpty()
    }

    fun <T : Enum<T>> getRandomEnumValue(enumClass: Class<T>): T {
        val values = enumClass.enumConstants
        return values[Random.nextInt(values.size)]
    }

    fun randomFilters(): Pair<PokemonType,PokemonType> {
        var randomPokemonType1 : PokemonType
        var randomPokemonType2 : PokemonType
        var filteredList = _pokemonList

        do {
            // Seleziona due tipi di Pokémon casuali
            randomPokemonType1 = getRandomEnumValue(PokemonType::class.java)
            randomPokemonType2 = getRandomEnumValue(PokemonType::class.java)

            // Applica il filtro con i tipi casuali selezionati
            filterPokemon(null, randomPokemonType1, randomPokemonType2)

            // Aggiorna la lista filtrata
            filteredList = _filteredPokemonList.value
        } while (filteredList.isEmpty())
        return Pair(randomPokemonType1,randomPokemonType2)
    }

}
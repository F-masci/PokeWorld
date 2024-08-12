package it.fale.pokeworld.entity.repository

import android.util.Log
import it.fale.pokeworld.entity.PokemonEntity

class PokemonRepository(private val dao: PokemonDao) {

    companion object {
        const val LOAD_ABILITIES = 0b001
        const val LOAD_MOVES = 0b010
        const val LOAD_ITEMS = 0b100
        const val LOAD_ALL = 0b111
    }

    suspend fun retrievePokemonList(): List<PokemonEntity> {
        Log.d("PokemonRepository", "Retrieving pokemon list")
        return dao.retrievePokemonList()
    }

    suspend fun retrievePokemon(pokemonId: Int, load: Int = 0): PokemonEntity {
        Log.d("PokemonRepository", "Retrieving pokemon with id $pokemonId")
        val p = dao.retrievePokemon(pokemonId)
        if(load and LOAD_ABILITIES != 0) p.abilities = dao.retrieveAbilitiesForPokemon(pokemonId)
        if(load and LOAD_MOVES != 0) p.moves = dao.retrieveMovesForPokemon(pokemonId)
        if(load and LOAD_ITEMS != 0) p.items = dao.retrieveItemsForPokemon(pokemonId)
        return p
    }

    suspend fun insertPokemon() {
        Log.d("PokemonRepository", "Inserting pokemon")
        dao.insertPokemon(PokemonEntity(1, "Bulbasaur"))
    }

}
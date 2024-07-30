package it.fale.pokeworld.entity.repository

import android.util.Log
import it.fale.pokeworld.entity.PokemonEntity

class PokemonRepository(private val dao: PokemonDao) {

    suspend fun retrievePokemonList(): List<PokemonEntity> {
        Log.d("PokemonRepository", "Retrieving pokemon list")
        return dao.retrievePokemonList()
    }

    suspend fun retrievePokemon(pokemonId: Int): PokemonEntity {
        Log.d("PokemonRepository", "Retrieving pokemon with id $pokemonId")
        return dao.retrievePokemon(pokemonId)
    }

    suspend fun retrievePokemonWithAbilities(pokemonId: Int): PokemonEntity {
        Log.d("PokemonRepository", "Retrieving pokemon and abilitie with id $pokemonId")
        val p = dao.retrievePokemon(pokemonId)
        p.abilities = dao.retrieveAbilitiesForPokemon(pokemonId)
        return p
    }

    suspend fun insertPokemon() {
        Log.d("PokemonRepository", "Inserting pokemon")
        dao.insertPokemon(PokemonEntity(1, "Bulbasaur"))
    }

}
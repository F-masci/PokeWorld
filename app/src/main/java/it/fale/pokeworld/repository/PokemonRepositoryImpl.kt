package it.fale.pokeworld.repository

import android.util.Log
import it.fale.pokeworld.entity.PokemonEntity

/**
 * Implementazione del repository per le operazioni di database.
 *
 * @param dao DAO per le operazioni di database.
 */
class PokemonRepositoryImpl(
    private val dao: PokemonDao
): PokemonRepository {

    /**
     * Ottiene la lista di tutti i pokemon presenti nel database.
     *
     * @return Lista di pokemon.
     */
    override suspend fun retrievePokemonList(): List<PokemonEntity> {
        Log.d("PokemonRepository", "Retrieving pokemon list")
        return dao.retrievePokemonList()
    }

    /**
     * Ottiene il pokemon con l'ID specificato.
     *
     * @param pokemonId ID del pokemon.
     * @param load Flag per specificare quali informazioni devono essere caricate nel dettaglio del pokemon. I possibili valori sono:
     * LOAD_ABILITIES, LOAD_MOVES, LOAD_ITEMS, LOAD_ALL (o loro combinazioni tramite OR).
     *
     * @return Pokemon con l'ID specificato.
     */
    override suspend fun retrievePokemon(pokemonId: Int, load: Int): PokemonEntity? {
        Log.d("PokemonRepository", "Retrieving pokemon with id $pokemonId")
        val p = dao.retrievePokemon(pokemonId)
        if(p != null) {
            if (load and PokemonRepository.LOAD_ABILITIES != 0) p.abilities = dao.retrieveAbilitiesForPokemon(pokemonId)
            if (load and PokemonRepository.LOAD_MOVES != 0) p.moves = dao.retrieveMovesForPokemon(pokemonId)
            if (load and PokemonRepository.LOAD_ITEMS != 0) p.items = dao.retrieveItemsForPokemon(pokemonId)
        }
        return p
    }

}
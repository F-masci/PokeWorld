package it.fale.pokeworld.repository

import it.fale.pokeworld.entity.PokemonEntity

/**
 * Interfaccia per le operazioni di accesso ai dati dei pokemon.
 */
interface PokemonRepository {

    companion object {

        // Flag per specificare quali informazioni devono essere caricate nel dettaglio del pokemon
        const val LOAD_ABILITIES = 0b001
        const val LOAD_MOVES = 0b010
        const val LOAD_ITEMS = 0b100
        const val LOAD_ALL = 0b111
    }

    /**
     * Ottiene la lista di tutti i pokemon presenti nel database.
     *
     * @return Lista di pokemon.
     */
    suspend fun retrievePokemonList(): List<PokemonEntity>

    /**
     * Ottiene il pokemon con l'ID specificato.
     *
     * @param pokemonId ID del pokemon.
     * @param load Flag per specificare quali informazioni devono essere caricate nel dettaglio del pokemon. I possibili valori sono:
     * LOAD_ABILITIES, LOAD_MOVES, LOAD_ITEMS, LOAD_ALL (o loro combinazioni tramite OR).
     *
     * @return Pokemon con l'ID specificato.
     */
    suspend fun retrievePokemon(pokemonId: Int, load: Int = 0): PokemonEntity?

}
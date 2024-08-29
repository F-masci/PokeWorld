package it.fale.pokeworld.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import it.fale.pokeworld.entity.AbilityEntity
import it.fale.pokeworld.entity.ItemEntity
import it.fale.pokeworld.entity.MoveEntity
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonTypeConverter

/**
 * Interfaccia per l'accesso al database
 */
@Dao
@TypeConverters(PokemonTypeConverter::class)
interface PokemonDao {

    /**
     * Ottiene la lista di tutti i Pokemon
     *
     * @return Lista di Pokemon
     */
    @Query("SELECT * FROM pokemon")
    suspend fun retrievePokemonList(): List<PokemonEntity>

    /**
     * Ottiene un Pokemon tramite il suo ID
     *
     * @param pokemonId ID del Pokemon
     * @return Il Pokemon con l'ID specificato
     */
    @Query("SELECT * FROM pokemon WHERE id = :pokemonId")
    suspend fun retrievePokemon(pokemonId: Int): PokemonEntity?


    /**
     * Ottiene le abilità di un Pokemon tramite il suo ID
     *
     * @param pokemonId ID del Pokemon
     * @return Lista di abilità del Pokemon
     */
    @Transaction
    @Query(
        """
        SELECT ability.* 
        FROM ability
        INNER JOIN pokemon_ability ON ability.id = pokemon_ability.abilityId
        WHERE pokemon_ability.pokemonId= :pokemonId
        """
    )
    suspend fun retrieveAbilitiesForPokemon(pokemonId: Int): List<AbilityEntity>

    /**
     * Ottiene gli item di un Pokemon tramite il suo ID
     *
     * @param pokemonId ID del Pokemon
     * @return Lista di item del Pokemon
     */
    @Transaction
    @Query(
        """
        SELECT item.* 
        FROM item
        INNER JOIN pokemon_item ON item.id = pokemon_item.itemId
        WHERE pokemon_item.pokemonId= :pokemonId
        """
    )
    suspend fun retrieveItemsForPokemon(pokemonId: Int): List<ItemEntity>

    /**
     * Ottiene le mosse di un Pokemon tramite il suo ID
     *
     * @param pokemonId ID del Pokemon
     * @return Lista di mosse del Pokemon
     */
    @Transaction
    @Query(
        """
        SELECT move.* 
        FROM move
        INNER JOIN pokemon_move ON move.id = pokemon_move.moveId
        WHERE pokemon_move.pokemonId= :pokemonId
        """
    )
    suspend fun retrieveMovesForPokemon(pokemonId: Int): List<MoveEntity>

}
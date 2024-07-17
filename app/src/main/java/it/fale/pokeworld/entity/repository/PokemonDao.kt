package it.fale.pokeworld.entity.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import it.fale.pokeworld.entity.AbilityEntity
import it.fale.pokeworld.entity.ItemEntity
import it.fale.pokeworld.entity.MoveEntity
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonTypeConverter
import it.fale.pokeworld.utils.TranslationListConverter

@Dao
@TypeConverters(PokemonTypeConverter::class)
interface PokemonDao {

    @Query("SELECT * FROM pokemon")
    suspend fun retrievePokemonList(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE id = :pokemonId")
    suspend fun retrievePokemon(pokemonId: Int): PokemonEntity

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

    @Insert
    suspend fun insertPokemon(pokemon: PokemonEntity)

}
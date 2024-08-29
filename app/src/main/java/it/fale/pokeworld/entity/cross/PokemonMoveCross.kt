package it.fale.pokeworld.entity.cross

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import it.fale.pokeworld.entity.MoveEntity
import it.fale.pokeworld.entity.PokemonEntity

/**
 * Classe che rappresenta la relazione tra un Pokemon e una mossa
 *
 * @param pokemonId ID del Pokemon
 * @param moveId ID della mossa
 */
@Entity(
    tableName = "pokemon_move",
    primaryKeys = ["pokemonId", "moveId"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MoveEntity::class,
            parentColumns = ["id"],
            childColumns = ["moveId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["pokemonId"]), Index(value = ["moveId"])]
)
data class PokemonMoveCross(
    val pokemonId: Int,
    val moveId: Int
)
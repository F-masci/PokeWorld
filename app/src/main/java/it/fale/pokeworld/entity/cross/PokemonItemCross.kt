package it.fale.pokeworld.entity.cross

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import it.fale.pokeworld.entity.ItemEntity
import it.fale.pokeworld.entity.PokemonEntity

@Entity(
    tableName = "pokemon_item",
    primaryKeys = ["pokemonId", "itemId"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["pokemonId"]), Index(value = ["itemId"])]
)
data class PokemonItemCross(
    val pokemonId: Int,
    val itemId: Int
)
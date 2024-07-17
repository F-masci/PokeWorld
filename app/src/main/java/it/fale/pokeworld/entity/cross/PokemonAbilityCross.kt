package it.fale.pokeworld.entity.cross

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import it.fale.pokeworld.entity.AbilityEntity
import it.fale.pokeworld.entity.PokemonEntity

@Entity(
    tableName = "pokemon_ability",
    primaryKeys = ["pokemonId", "abilityId"],
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AbilityEntity::class,
            parentColumns = ["id"],
            childColumns = ["abilityId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["pokemonId"]), Index(value = ["abilityId"])]
)
data class PokemonAbilityCross(
    val pokemonId: Int,
    val abilityId: Int
)
package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(

    /* --- GENERAL --- */

    @PrimaryKey val id: Int,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,

    /* --- SPRITES --- */

    val spriteDefault: String? = null,
    val spriteShiny: String? = null,

    val showdownDefault: String? = null,
    val showdownShiny: String? = null,

    val criesLegacy: String? = null,
    val criesLatest: String? = null,

    /* --- TYPES --- */

    val type1: PokemonType? = null,
    val type2: PokemonType? = null,

    /* --- STATS --- */

    val hp: Int = 0,
    val attack: Int = 0,
    val defense: Int = 0,
    val specialAttack: Int = 0,
    val specialDefense: Int =0,
    val speed: Int = 0

) {

    /* --- ABILITIES --- */

    @Ignore
    var abilities: List<AbilityEntity> = emptyList()

    /* --- ITEMS --- */

    @Ignore
    var items: List<ItemEntity> = emptyList()

    /* --- MOVES --- */

    @Ignore
    var moves: List<MoveEntity> = emptyList()

    fun getImageUrl(): String? {
        if(showdownDefault != null) return showdownDefault
        return spriteDefault
    }

}
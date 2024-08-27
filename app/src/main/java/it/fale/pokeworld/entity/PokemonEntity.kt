package it.fale.pokeworld.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Classe che rappresenta un Pokemon
 */
@Entity(tableName = "pokemon")
data class PokemonEntity(

    /* --- GENERAL --- */

    @PrimaryKey val id: Int,
    val name: String,
    val height: Int = 0,
    val weight: Int = 0,

    /* --- SPRITES --- */
    // Non contiene direttamente gli sprites ma contiene i link

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

    /**
     * Ottiene l'url dell'immagine dinamica da mostrare
     *
     * @return l'url dell'immagine dinamica da mostrare
     */
    fun getAnimatedImageUrl(): String? {
        // Controlla se ci sia un'immagina dinamica
        if(showdownDefault != null) return showdownDefault
        // Se non esiste viene ritornata un'immagina statica
        return getImageUrl()
    }

    /**
     * Ottiene l'url dell'immagine statica da mostrare
     *
     * @return l'url dell'immagine statica da mostrare
     */
    fun getImageUrl(): String? {
        return spriteDefault
    }

}
package it.fale.pokeworld.entity

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.room.TypeConverter
import it.fale.pokeworld.R
import it.fale.pokeworld.ui.theme.LocalThemedColorsPalette
import kotlin.random.Random

/**
 * Tipo di Pokemon
 *
 * @param type Codice del tipo di Pokemon
 * @param string Stringa da utilizzare come titolo
 * @param icon Icona da utilizzare
 * @param getBackgroundColor Funzione per ottenere il colore di sfondo da utilizzare
 * @param getBackgroundTextColor Funzione per ottenere il colore del testo da utilizzare
 */
enum class PokemonType(
    val type: String,
    val string: Int,
    val icon: Int,
    val getBackgroundColor: @Composable () -> Color = { Color.Unspecified },
    val getBackgroundTextColor: @Composable () -> Color = { Color.Unspecified }
) {

    NORMAL("normal", R.string.normal, R.drawable.normal,{ LocalThemedColorsPalette.current.normalBackground }, { LocalThemedColorsPalette.current.normalTextBackground }),
    FIGHTING("fighting", R.string.fighting, R.drawable.fighting, { LocalThemedColorsPalette.current.fightingBackground }, { LocalThemedColorsPalette.current.fightingTextBackground }),
    FLYING("flying", R.string.flying, R.drawable.flying, { LocalThemedColorsPalette.current.flyingBackground }, { LocalThemedColorsPalette.current.flyingTextBackground }),
    POISON("poison", R.string.poison, R.drawable.poison, { LocalThemedColorsPalette.current.poisonBackground }, { LocalThemedColorsPalette.current.poisonTextBackground }),
    GROUND("ground", R.string.ground, R.drawable.ground, { LocalThemedColorsPalette.current.groundBackground }, { LocalThemedColorsPalette.current.groundTextBackground }),
    ROCK("rock", R.string.rock, R.drawable.rock, { LocalThemedColorsPalette.current.rockBackground }, { LocalThemedColorsPalette.current.rockTextBackground }),
    BUG("bug", R.string.bug, R.drawable.bug, { LocalThemedColorsPalette.current.bugBackground }, { LocalThemedColorsPalette.current.bugTextBackground }),
    GHOST("ghost", R.string.ghost, R.drawable.ghost, { LocalThemedColorsPalette.current.ghostBackground }, { LocalThemedColorsPalette.current.ghostTextBackground }),
    STEEL("steel", R.string.steel, R.drawable.steel, { LocalThemedColorsPalette.current.steelBackground }, { LocalThemedColorsPalette.current.steelTextBackground }),
    FIRE("fire", R.string.fire, R.drawable.fire, { LocalThemedColorsPalette.current.fireBackground }, { LocalThemedColorsPalette.current.fireTextBackground }),
    WATER("water", R.string.water, R.drawable.water, { LocalThemedColorsPalette.current.waterBackground }, { LocalThemedColorsPalette.current.waterTextBackground }),
    GRASS("grass", R.string.grass, R.drawable.grass, { LocalThemedColorsPalette.current.grassBackground }, { LocalThemedColorsPalette.current.grassTextBackground }),
    ELECTRIC("electric", R.string.electric, R.drawable.electric, { LocalThemedColorsPalette.current.electricBackground }, { LocalThemedColorsPalette.current.electricTextBackground }),
    PSYCHIC("psychic", R.string.psychic, R.drawable.psychic, { LocalThemedColorsPalette.current.psychicBackground }, { LocalThemedColorsPalette.current.psychicTextBackground }),
    ICE("ice", R.string.ice, R.drawable.ice, { LocalThemedColorsPalette.current.iceBackground }, { LocalThemedColorsPalette.current.iceTextBackground }),
    DRAGON("dragon", R.string.dragon, R.drawable.dragon, { LocalThemedColorsPalette.current.dragonBackground }, { LocalThemedColorsPalette.current.dragonTextBackground }),
    DARK("dark", R.string.dark, R.drawable.dark, { LocalThemedColorsPalette.current.darkBackground }, { LocalThemedColorsPalette.current.darkTextBackground }),
    FAIRY("fairy", R.string.fairy, R.drawable.fairy, { LocalThemedColorsPalette.current.fairyBackground }, { LocalThemedColorsPalette.current.fairyTextBackground }),
    STELLAR("stellar", R.string.stellar, R.drawable.stellar, { LocalThemedColorsPalette.current.stellarBackground }, { LocalThemedColorsPalette.current.stellarTextBackground }),
    UNKNOWN("unknown", R.string.unknown, R.drawable.normal, { LocalThemedColorsPalette.current.normalBackground }, { LocalThemedColorsPalette.current.normalTextBackground }),
    SHADOW("shadow", R.string.shadow, R.drawable.normal, { LocalThemedColorsPalette.current.shadowBackground }, { LocalThemedColorsPalette.current.shadowTextBackground });

    companion object {

        /**
         * Ottiene il tipo di Pokemon da un codice
         *
         * @param type Codice del tipo di Pokemon
         *
         * @return Il tipo di Pokemon corrispondente al codice
         */
        fun fromType(type: String): PokemonType? {
            return entries.find { it.type == type }
        }

        /**
         * Ottiene un tipo di Pokemon casuale
         *
         * @return Il tipo di Pokemon casuale
         */
        fun getRandomPokemonType(): PokemonType {
            val values = PokemonType.entries
            val randomIndex = Random.nextInt(values.size)
            return values[randomIndex]
        }
    }

}

/**
 * Converte un tipo di Pokemon in una stringa
 */
class PokemonTypeConverter {

    /**
     * Converte un tipo di Pokemon in una stringa
     *
     * @param type Il tipo di Pokemon da convertire
     *
     * @return La stringa corrispondente al tipo di Pokemon
     */
    @TypeConverter
    fun fromPokemonType(type: PokemonType?): String? {
        return type?.type
    }

    /**
     * Converte una stringa in un tipo di Pokemon
     *
     * @param type La stringa da convertire
     *
     * @return Il tipo di Pokemon corrispondente alla stringa
     */
    @TypeConverter
    fun toPokemonType(type: String): PokemonType? {
        return PokemonType.fromType(type)
    }

}
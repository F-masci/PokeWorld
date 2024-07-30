package it.fale.pokeworld.entity

import android.util.Log
import androidx.room.TypeConverter
import it.fale.pokeworld.R

enum class PokemonType(val type: String, val resource: Int) {

    NORMAL("normal", R.string.normal),
    FIGHTING("fighting", R.string.fighting),
    FLYING("flying", R.string.flying),
    POISON("poison", R.string.poison),
    GROUND("ground", R.string.ground),
    ROCK("rock", R.string.rock),
    BUG("bug", R.string.bug),
    GHOST("ghost", R.string.ghost),
    STEEL("steel", R.string.steel),
    FIRE("fire", R.string.fire),
    WATER("water", R.string.water),
    GRASS("grass", R.string.grass),
    ELECTRIC("electric", R.string.electric),
    PSYCHIC("psychic", R.string.psychic),
    ICE("ice", R.string.ice),
    DRAGON("dragon", R.string.dragon),
    DARK("dark", R.string.dark),
    FAIRY("fairy", R.string.fairy),
    STELLAR("stellar", R.string.stellar),
    UNKNOWN("unknown", R.string.unknown),
    SHADOW("shadow", R.string.shadow);

    companion object {
        fun fromString(type: String): PokemonType? {
            //Log.d("PokemonTypeConverter", "Converting $type to PokemonType")
            return entries.find { it.type == type }
        }
    }

}

class PokemonTypeConverter {

    @TypeConverter
    fun fromPokemonType(type: PokemonType?): String? {
        return type?.type
    }

    @TypeConverter
    fun toPokemonType(type: String): PokemonType? {
        return PokemonType.fromString(type)
    }

}
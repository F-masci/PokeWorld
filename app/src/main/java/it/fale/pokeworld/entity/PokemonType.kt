package it.fale.pokeworld.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.room.TypeConverter
import it.fale.pokeworld.R

enum class PokemonType(val type: String, val string: Int, val icon: Int, val backgroundColor: Int, val textColor: Int) {

    NORMAL("normal", R.string.normal, R.drawable.normal, R.color.normal_light_background, R.color.normal_light_text),
    FIGHTING("fighting", R.string.fighting, R.drawable.fighting, R.color.fighting_light_background, R.color.fighting_light_text),
    FLYING("flying", R.string.flying, R.drawable.flying, R.color.flying_light_background, R.color.flying_light_text),
    POISON("poison", R.string.poison, R.drawable.poison, R.color.poison_light_background, R.color.poison_light_text),
    GROUND("ground", R.string.ground, R.drawable.ground, R.color.ground_light_background, R.color.ground_light_text),
    ROCK("rock", R.string.rock, R.drawable.rock, R.color.rock_light_background, R.color.rock_light_text),
    BUG("bug", R.string.bug, R.drawable.bug, R.color.bug_light_background, R.color.bug_light_text),
    GHOST("ghost", R.string.ghost, R.drawable.ghost, R.color.ghost_light_background, R.color.ghost_light_text),
    STEEL("steel", R.string.steel, R.drawable.steel, R.color.steel_light_background, R.color.steel_light_text),
    FIRE("fire", R.string.fire, R.drawable.fire, R.color.fire_light_background, R.color.fire_light_text),
    WATER("water", R.string.water, R.drawable.water, R.color.water_light_background, R.color.water_light_text),
    GRASS("grass", R.string.grass, R.drawable.grass, R.color.grass_light_background, R.color.grass_light_text),
    ELECTRIC("electric", R.string.electric, R.drawable.electric, R.color.electric_light_background, R.color.electric_light_text),
    PSYCHIC("psychic", R.string.psychic, R.drawable.psychic, R.color.psychic_light_background, R.color.psychic_light_text),
    ICE("ice", R.string.ice, R.drawable.ice, R.color.ice_light_background, R.color.ice_light_text),
    DRAGON("dragon", R.string.dragon, R.drawable.dragon, R.color.dragon_light_background, R.color.dragon_light_text),
    DARK("dark", R.string.dark, R.drawable.dark, R.color.dark_light_background, R.color.dark_light_text),
    FAIRY("fairy", R.string.fairy, R.drawable.fairy, R.color.fairy_light_background, R.color.fairy_light_text),
    STELLAR("stellar", R.string.stellar, R.drawable.stellar, R.color.stellar_light_background, R.color.stellar_light_text),
    UNKNOWN("unknown", R.string.unknown, R.drawable.normal, R.color.normal_light_background, R.color.normal_light_text),
    SHADOW("shadow", R.string.shadow, R.drawable.normal, R.color.shadow_light_background, R.color.shadow_light_text);

    companion object {
        fun fromString(type: String): PokemonType? {
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
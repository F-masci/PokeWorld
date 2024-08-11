package it.fale.pokeworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.entity.PokemonTypeConverter
import it.fale.pokeworld.entity.repository.PokemonDatabase
import it.fale.pokeworld.entity.repository.PokemonRepository
import it.fale.pokeworld.ui.theme.PokeWorldTheme
import it.fale.pokeworld.viewmodel.PokemonListViewModel

class PokeWorldActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val repository = PokemonRepository(PokemonDatabase.getInstance(LocalContext.current).pokemonDao())
            val pokemonListViewModel = PokemonListViewModel(repository)

            PokeWorldTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokemonList(pokemonListViewModel)
                }
            }
        }
    }
}

@Composable
fun PokemonList(pokemonListViewModel: PokemonListViewModel) {

    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        content = {
            items(pokemonList.value) { pokemon ->
                PokemonCard(
                    pokemon = pokemon, modifier = Modifier
                        .padding(20.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(10))
                        .background(
                            if (pokemon.type1 != null) getBackgroundColorForType(type = pokemon.type1) else Color.Magenta,
                            RoundedCornerShape(10)
                        )
                        .height(250.dp)
                        .width(250.dp)
                    )
            }
        }
    )
}

@Composable
fun PokemonDetail(pokemon: PokemonEntity, modifier: Modifier) {

    var txt = pokemon.name
    if(pokemon.type1 !== null) txt += " - " + stringResource(id = pokemon.type1.resource)
    if(pokemon.type2 !== null) txt += " - " + stringResource(id = pokemon.type2.resource)

    Column(modifier = modifier) {
        Row {
            Text(pokemon.name)
        }
        if(pokemon.type1 !== null)
            Row {
                Text("Tipo 1: " + stringResource(id = pokemon.type1.resource))
            }
        if(pokemon.type2 !== null)
            Row {
                Text("Tipo 2: " + stringResource(id = pokemon.type2.resource))
            }
    }

}

@Composable
fun PokemonCard(pokemon: PokemonEntity, modifier: Modifier) {

    var name = pokemon.name
    var spriteUrl = pokemon.spriteDefault

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Row {
            Text(name, fontSize = 15.sp)
        }
        AsyncImage(
            model = spriteUrl,
            contentDescription = null,
            modifier = Modifier
                .height(140.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(10))
        )
        if(pokemon.type1 !== null) TypeRow(type = pokemon.type1)
        if(pokemon.type2 !== null) TypeRow(type = pokemon.type2)
    }
}

@Composable
fun TypeRow(type: PokemonType) {

    Row (modifier = Modifier
        .background(getTextColorForType(type = type).copy(alpha = 0.8f), RoundedCornerShape(30))
        .width(135.dp)
        .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Image(painterResource(id = getIconForType(type)), "type_icon",
            modifier = Modifier
                .padding(2.dp)
                .height(20.dp))
        Text(stringResource(id = type.resource), fontSize = 12.sp)
    }
}

fun getIconForType(type: PokemonType): Int {
    return when(type){
        PokemonType.GRASS -> R.drawable.grass
        PokemonType.FIGHTING -> R.drawable.fighting
        PokemonType.FLYING -> R.drawable.flying
        PokemonType.POISON -> R.drawable.poison
        PokemonType.GROUND -> R.drawable.ground
        PokemonType.ROCK -> R.drawable.rock
        PokemonType.BUG -> R.drawable.bug
        PokemonType.GHOST -> R.drawable.ghost
        PokemonType.STEEL -> R.drawable.steel
        PokemonType.FIRE -> R.drawable.fire
        PokemonType.WATER -> R.drawable.water
        PokemonType.ELECTRIC -> R.drawable.electric
        PokemonType.PSYCHIC -> R.drawable.psychic
        PokemonType.ICE -> R.drawable.ice
        PokemonType.DRAGON -> R.drawable.dragon
        PokemonType.DARK -> R.drawable.dark
        PokemonType.FAIRY -> R.drawable.fairy
        PokemonType.STELLAR -> R.drawable.stellar
        PokemonType.NORMAL -> R.drawable.normal
        else -> R.drawable.normal
    }
}

@Composable
fun getBackgroundColorForType(type: PokemonType) : Color {
    return when(type){
        PokemonType.GRASS -> colorResource(R.color.grass_light_background)
        PokemonType.FIGHTING -> colorResource(R.color.fighting_light_background)
        PokemonType.FLYING -> colorResource(R.color.flying_light_background)
        PokemonType.POISON -> colorResource(R.color.poison_light_background)
        PokemonType.GROUND -> colorResource(R.color.ground_light_background)
        PokemonType.ROCK -> colorResource(R.color.rock_light_background)
        PokemonType.BUG -> colorResource(R.color.bug_light_background)
        PokemonType.GHOST -> colorResource(R.color.ghost_light_background)
        PokemonType.STEEL -> colorResource(R.color.steel_light_background)
        PokemonType.FIRE -> colorResource(R.color.fire_light_background)
        PokemonType.WATER -> colorResource(R.color.water_light_background)
        PokemonType.ELECTRIC -> colorResource(R.color.electric_light_background)
        PokemonType.PSYCHIC -> colorResource(R.color.psychic_light_background)
        PokemonType.ICE -> colorResource(R.color.ice_light_background)
        PokemonType.DRAGON -> colorResource(R.color.dragon_light_background)
        PokemonType.DARK -> colorResource(R.color.dark_light_background)
        PokemonType.FAIRY -> colorResource(R.color.fairy_light_background)
        PokemonType.STELLAR -> colorResource(R.color.stellar_light_background)
        PokemonType.SHADOW -> colorResource(R.color.shadow_light_background)
        else -> Color.LightGray
    }
}

@Composable
fun getTextColorForType(type: PokemonType) : Color {
    return when(type){
        PokemonType.GRASS -> colorResource(R.color.grass_light_text)
        PokemonType.FIGHTING -> colorResource(R.color.fighting_light_text)
        PokemonType.FLYING -> colorResource(R.color.flying_light_text)
        PokemonType.POISON -> colorResource(R.color.poison_light_text)
        PokemonType.GROUND -> colorResource(R.color.ground_light_text)
        PokemonType.ROCK -> colorResource(R.color.rock_light_text)
        PokemonType.BUG -> colorResource(R.color.bug_light_text)
        PokemonType.GHOST -> colorResource(R.color.ghost_light_text)
        PokemonType.STEEL -> colorResource(R.color.steel_light_text)
        PokemonType.FIRE -> colorResource(R.color.fire_light_text)
        PokemonType.WATER -> colorResource(R.color.water_light_text)
        PokemonType.ELECTRIC -> colorResource(R.color.electric_light_text)
        PokemonType.PSYCHIC -> colorResource(R.color.psychic_light_text)
        PokemonType.ICE -> colorResource(R.color.ice_light_text)
        PokemonType.DRAGON -> colorResource(R.color.dragon_light_text)
        PokemonType.DARK -> colorResource(R.color.dark_light_text)
        PokemonType.FAIRY -> colorResource(R.color.fairy_light_text)
        PokemonType.STELLAR -> colorResource(R.color.stellar_light_text)
        PokemonType.SHADOW -> colorResource(R.color.shadow_light_text)
        else -> Color.Gray
    }
}


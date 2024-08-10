package it.fale.pokeworld.listScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.viewmodel.PokemonListViewModel

//vecchio sistema di avvio che temporaneamente ho commentato

//class PokeWorldActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//
//            val repository = PokemonRepository(PokemonDatabase.getInstance(LocalContext.current).pokemonDao())
//            val pokemonListViewModel = PokemonListViewModel(repository)
//
//            PokeWorldTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    PokemonList(pokemonListViewModel)
//                }
//            }
//        }
//    }
//}



@Composable
fun PokemonList(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel
) {
    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()
    Surface(//ho mantenuto il meccanismo di Surface come era nel vecchio push, ma l ho esso direttamente qui(per vedere come era prima guarda anche la parte sopra commentata)
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(Color.Red),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.width(55.dp))
                AsyncImage(model = R.drawable.logo, contentDescription = null)
                IconButton(
                    onClick = { navController.navigate("settings_screen") },
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings",
                        modifier = Modifier
                            .size(100.dp)
                    )
                }
            }
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
                            .width(250.dp),
                        onClick = {
                            navController.navigate("pokemon_detail/${pokemon.id}")
                        }                   //questa notazione è fatta in modo tale che posso passare comunque un argomento (l'id)
                    )                       //senza che nella MainActivity vado a definire
                }
            }
            )
        }
    }
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
fun PokemonCard(pokemon: PokemonEntity, modifier: Modifier,onClick: () -> Unit) {

    var name = pokemon.name
    var spriteUrl = pokemon.spriteDefault

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Row {
            Text(name)
        }
        AsyncImage(
            model = spriteUrl,
            contentDescription = null,
            modifier = Modifier
                .clickable(onClick = onClick)
                .height(140.dp)
                .background(Color.White, RoundedCornerShape(10))
        )
        if(pokemon.type1 !== null)
            Row (modifier = Modifier
                .background(getTextColorForType(type = pokemon.type1), RoundedCornerShape(10))
                .width(120.dp)){
                Text(stringResource(id = pokemon.type1.resource))
            }
        if(pokemon.type2 !== null)
            Row (modifier = Modifier
                .background(getTextColorForType(type = pokemon.type2), RoundedCornerShape(10))
                .width(120.dp)){
                Text(stringResource(id = pokemon.type2.resource))
            }
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


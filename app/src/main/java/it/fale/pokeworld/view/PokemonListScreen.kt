package it.fale.pokeworld

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.pokemonPixelFont
import it.fale.pokeworld.viewmodel.PokemonListViewModel



@Composable
fun PokemonList(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel
) {
    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()
    var isSearchBarVisible by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            TopBar(navController = navController, onSearchClicked = {
                isSearchBarVisible = !isSearchBarVisible
            })
            if (isSearchBarVisible) {
                SearchBar()
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
                                navController.navigate("pokemon_details_screen/${pokemon.id}")
                            }                   //questa notazione è fatta in modo tale che posso passare comunque un argomento (l'id)
                        )                       //senza che nella MainActivity vado a definire
                    }
                }

            )
        }
    }
}



@Composable
fun SearchBar() {
    var query by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = query,
                onValueChange = { newValue ->
                    query = newValue
                },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .heightIn(min = 56.dp, max = 56.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            ChoiceTypeMenu(
                initialText = "Select Type",
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = {},//dobbiamo inserire  filtraggio
                options = listOf("Option A", "Option B")//da modificare per pokemon
            )
            ChoiceTypeMenu(
                initialText = "Select Type",
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = {},
                options = listOf("Option A", "Option B")//da modificare per pokemon
            )
        }
    }
}

@Composable
fun ChoiceTypeMenu(
    initialText: String,
    expandedState: MutableState<Boolean>,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
) {
    var selectedOption by remember { mutableStateOf(initialText) }

    Box {
        Button(
            onClick = { expandedState.value = !expandedState.value },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .background(Color.Red, RoundedCornerShape(30))
                .width(166.dp)
        ) {
            Text(
                selectedOption,
                fontSize = 10.sp,
                color = Color.White,
                fontFamily = pokemonPixelFont,
            )
        }
        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false },
            modifier = Modifier
                .width(166.dp)//Per ora l'ho impostato manualmente,dato che non ho trovato una funziona che sincronizza con  Button
        ) {
//      possibile reset
//            DropdownMenuItem({Text("Reset", color = Color.Red)},onClick = {
//                selectedOption = initialText // Reset del testo del pulsante al valore iniziale
//                onOptionSelected(initialText)
//                expandedState.value = false
//            })
            options.forEach { option ->
                DropdownMenuItem({ Text(option,  fontSize = 10.sp,fontFamily = pokemonPixelFont) },onClick = {
                    selectedOption = option
                    onOptionSelected(option)
                    expandedState.value = false
                })
            }
        }
    }
}


@Composable
fun TopBar(navController: NavController, onSearchClicked: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Red),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onSearchClicked,
            modifier = Modifier.size(60.dp)) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
            )
        }
        AsyncImage(model = R.drawable.logo, contentDescription = null, modifier = Modifier.height(40.dp))
        IconButton(
            onClick = { navController.navigate("settings_screen")},
            modifier = Modifier.size(60.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.prova2),
                contentDescription = "Settings",
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
fun PokemonCard(pokemon: PokemonEntity, modifier: Modifier, onClick: () -> Unit) {

    val name = pokemon.name
    val spriteUrl = pokemon.spriteDefault

    Column(modifier = modifier
            .clickable { onClick() },
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


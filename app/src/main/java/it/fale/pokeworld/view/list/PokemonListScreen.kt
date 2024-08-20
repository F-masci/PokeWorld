package it.fale.pokeworld.view.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.entity.PokemonTypeConverter
import it.fale.pokeworld.ui.theme.pokemonPixelFont
import it.fale.pokeworld.view.TypeRow
import it.fale.pokeworld.viewmodel.PokemonListViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PokemonListScreen(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel
) {
    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(false) } // Stato del tema

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Variabili per i filtri
    var query by remember { mutableStateOf("") }
    var selectedType1 by rememberSaveable { mutableStateOf<PokemonType?>(null) }
    var selectedType2 by rememberSaveable { mutableStateOf<PokemonType?>(null) }

    if(pokemonList.value.isEmpty())
        SplashScreen()
    else
        SettingsDrawer(
            drawerState = drawerState,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = if (isDarkTheme) Color.DarkGray else Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                ) {
                    TopBar(
                        navController = navController,
                        onSearchClicked = {
                            isSearchBarVisible = !isSearchBarVisible
                        },
                        onSettingsClicked = {
                            openDrawer()
                        }
                    )
                    if (isSearchBarVisible) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = query,
                                    onValueChange = { newValue ->
                                        query = newValue
                                        pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2)
                                    },
                                    placeholder = { Text("Search...") },
                                    modifier = Modifier
                                        .heightIn(min = 56.dp, max = 56.dp)
                                        .width(290.dp),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedIndicatorColor = colorResource(id = R.color.pokemon_blue),
                                        unfocusedPlaceholderColor = colorResource(id = R.color.pokemon_blue),
                                        unfocusedTextColor = colorResource(id = R.color.pokemon_blue),
                                        focusedIndicatorColor = colorResource(id = R.color.light_pokemon_blue),
                                        focusedContainerColor = colorResource(id = R.color.light_pokemon_blue),
                                        focusedTextColor = colorResource(id = R.color.light_pokemon_yellow),
                                        focusedPlaceholderColor = colorResource(id = R.color.light_pokemon_yellow),
                                        unfocusedContainerColor = colorResource(id = R.color.pokemon_yellow)
                                    ),
                                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                // Bottone per reimpostare i filtri a "any"
                                Button(onClick = {
                                    query = ""
                                    selectedType1 = null // Resetta il valore del filtro
                                    selectedType2 = null // Resetta il valore del filtro
                                    pokemonListViewModel.filterPokemon(null, null, null)
                                },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .background(colorResource(id = R.color.pokemon_yellow), RoundedCornerShape(100))
                                        .width(70.dp)
                                        .height(50.dp)) {
                                    Image(painterResource(id = R.drawable.reset), "reset", modifier = Modifier.size(80.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                ChoiceTypeMenu(
                                    type = selectedType1,
                                    expandedState = remember { mutableStateOf(false) },
                                    onOptionSelected = { selectedOption ->
                                        selectedType1 = PokemonType.fromString(selectedOption)
                                        pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2)
                                    },
                                    options = listOf("any") + (PokemonType.entries.map { it.type })
                                )
                                Spacer(modifier = Modifier.width(30.dp))
                                ChoiceTypeMenu(
                                    type = selectedType2,
                                    expandedState = remember { mutableStateOf(false) },
                                    onOptionSelected = { selectedOption ->
                                        selectedType2 = PokemonType.fromString(selectedOption)
                                        pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2)
                                    },
                                    options = listOf("any") + (PokemonType.entries.map { it.type })
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                //Pulsante per la scelta casuale
                                Button(onClick = {
                                    val (type1, type2) = pokemonListViewModel.randomFilters()
                                    selectedType1 = type1
                                    selectedType2 = type2
                                    pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2)
                                },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .background(
                                            colorResource(id = R.color.pokemon_yellow),
                                            RoundedCornerShape(100)
                                        )
                                        .width(70.dp)
                                        .height(50.dp)){
                                    Image(painterResource(id = R.drawable.random), "random", modifier = Modifier.size(70.dp))
                                }
                            }
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(200.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        content = {
                            items(pokemonList.value) { pokemon ->
                                PokemonCard(
                                    pokemon = pokemon,
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .border(2.dp, Color.DarkGray, RoundedCornerShape(10))
                                        .height(310.dp)
                                        .width(250.dp)
                                        .background(
                                            if (pokemon.type1 != null) colorResource(id = pokemon.type1.backgroundColor)
                                            else Color.Magenta,
                                            RoundedCornerShape(10)
                                        ),
                                    onClick = {
                                        navController.navigate("pokemon_details_screen/${pokemon.id}")
                                    }
                                )
                            }
                        }

                    )
                }
            }
        }
}

@Composable
fun ChoiceTypeMenu(
    type: PokemonType?,
    expandedState: MutableState<Boolean>,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
) {
    var selectedOption = type?.type ?: "any"
    var selectedColor = type?.backgroundTextColor ?: R.color.light_pokemon_blue

    // Il testo visualizzato viene ora gestito dal componente genitore
    Box {
        Button(
            onClick = { expandedState.value = !expandedState.value },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .background(color = colorResource(id = selectedColor), RoundedCornerShape(10.dp))
                .width(130.dp)
        ) {
            //Image(painterResource(id = PokemonTypeConverter().toPokemonType(selectedOption)!!.icon), "icon")
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
                .width(180.dp)//Per ora l'ho impostato manualmente,dato che non ho trovato una funziona che sincronizza con  Button
                .height(300.dp)
        ) {
//      possibile reset
//            DropdownMenuItem({Text("Reset", color = Color.Red)},onClick = {
//                selectedOption = initialText // Reset del testo del pulsante al valore iniziale
//                onOptionSelected(initialText)
//                expandedState.value = false
//            })
            options.forEach { option ->
                DropdownMenuItem(
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxSize(1f)
                                .height(30.dp)
                                .background(
                                    if (option == "any") Color.White
                                    else colorResource(
                                        id = PokemonTypeConverter().toPokemonType(
                                            option
                                        )!!.backgroundTextColor
                                    ), RoundedCornerShape(10.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            if(option != "any") {
                                Image(painterResource(id = PokemonTypeConverter().toPokemonType(option)!!.icon), "icon", modifier = Modifier.height(25.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                            Text(option,
                                fontSize = 12.sp,fontFamily = pokemonPixelFont)
                        }
                    },
                    onClick = {
                    selectedOption = option
                    if(selectedOption == "any") selectedColor = R.color.light_pokemon_blue
                    else selectedColor = PokemonTypeConverter().toPokemonType(option)!!.backgroundTextColor
                    onOptionSelected(option)
                    expandedState.value = false
                })
            }
        }
    }
}

//Tentativo di aggiunta modale laterale

@Composable
fun TopBar(
    navController: NavController,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
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
            onClick=onSettingsClicked,
            //onClick = { navController.navigate("settings_screen")},
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
fun PokemonCard(pokemon: PokemonEntity, modifier: Modifier, onClick: () -> Unit) {

    Column(modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly) {
        Row {
            Text(pokemon.name, fontSize = 15.sp)
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemon.getAnimatedImageUrl())
                .decoderFactory(ImageDecoderDecoder.Factory())
                .build(),
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
fun SplashScreen() {

    val gradient = Brush.radialGradient(
        0.0f to colorResource(id = R.color.gradient_light),
        0.8f to colorResource(id = R.color.gradient_dark),
        radius = 1800.0f,
        tileMode = TileMode.Repeated)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberDrawablePainter(
                drawable = getDrawable(
                    LocalContext.current,
                    R.drawable.pikachu
                )
            ),
            contentDescription = "content description",
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Image(painterResource(id = R.drawable.logo2),
            contentDescription = "logo",
            modifier = Modifier
                .size(180.dp)
                .fillMaxHeight())

    }
}
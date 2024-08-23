package it.fale.pokeworld.view.list

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
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
import kotlinx.coroutines.launch

@Composable
fun PokemonListScreen(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel
) {

    val context = LocalContext.current
    var isDarkTheme by remember { mutableStateOf(pokemonListViewModel.getThemePreference(context)) }

    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()
    var isSearchBarVisible by remember { mutableStateOf(false) }

    // Variabile per memorizzare lo stato della LazyVerticalGrid, che permette di controllare la posizione di scroll e del drawe
    val scope = rememberCoroutineScope()
    val pokemonListState = rememberLazyGridState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Variabili per i filtri
    var query by remember { mutableStateOf("") }
    var selectedType1 by rememberSaveable { mutableStateOf<PokemonType?>(null) }
    var selectedType2 by rememberSaveable { mutableStateOf<PokemonType?>(null) }

    if(pokemonList.value.isEmpty() && query.isBlank() && selectedType1 == null && selectedType2 == null)
        SplashScreen()
    else
        SettingsDrawer(
            drawerState = drawerState,
            isDarkTheme = isDarkTheme,
            onThemeToggle = { newTheme ->
                isDarkTheme = newTheme
                pokemonListViewModel.saveThemePreference(context, isDarkTheme)
            }
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
                        onSearchClicked = {
                            isSearchBarVisible = !isSearchBarVisible
                        },
                        onSettingsClicked = {
                            scope.launch { drawerState.open() }
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
                                        .weight(1f),
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
                                Spacer(modifier = Modifier.width(10.dp))

                                Button(onClick = {
                                    query = ""
                                    selectedType1 = null // Resetta il valore del filtro
                                    selectedType2 = null // Resetta il valore del filtro
                                    pokemonListViewModel.filterPokemon(null, null, null)
                                },
                                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                                    modifier = Modifier
                                        .background(
                                            colorResource(id = R.color.pokemon_yellow),
                                            RoundedCornerShape(100)
                                        )
                                        .width(70.dp)
                                        .height(50.dp)) {
                                    Icon(painterResource(id = R.drawable.reset), "reset", tint = colorResource(id = R.color.pokemon_blue), modifier = Modifier.size(80.dp))
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
                                    options = listOf("any") + (PokemonType.entries.map { it.type }),
                                    modifier = Modifier
                                        .weight(1f)
//                                        .padding(end = 15.dp)

                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                ChoiceTypeMenu(
                                    type = selectedType2,
                                    expandedState = remember { mutableStateOf(false) },
                                    onOptionSelected = { selectedOption ->
                                        selectedType2 = PokemonType.fromString(selectedOption)
                                        pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2)
                                    },
                                    options = listOf("any") + (PokemonType.entries.map { it.type }),
                                    modifier = Modifier
                                        .weight(1f)
//                                        .padding(end = 15.dp)

                                )
                                Spacer(modifier = Modifier.width(10.dp))
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
                                    Icon(painterResource(id = R.drawable.random), "random", tint = colorResource(id = R.color.pokemon_blue), modifier = Modifier.size(70.dp))
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight(1f)
                            .fillMaxWidth(1f)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(200.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            state = pokemonListState,
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

                        IconButton(
                            onClick = {
                                scope.launch {
                                    pokemonListState.animateScrollToItem(index = 0)
                                }
                            }, modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(6.dp)
                                .background(
                                    colorResource(id = R.color.pokemon_yellow),
                                    RoundedCornerShape(100.dp)
                                )
                                .padding(3.dp)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.uparrow), "uparrow",
                                tint = colorResource(id = R.color.pokemon_blue)
                            )
                        }
                    }
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
    modifier: Modifier
) {
    var selectedOption = type?.type ?: "any"
    var selectedColor = type?.backgroundTextColor ?: R.color.light_pokemon_blue
    var buttonPosition by remember { mutableStateOf(IntOffset(0, 0)) }
    var buttonSize by remember { mutableStateOf(IntSize.Zero)}
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        // Il testo visualizzato viene ora gestito dal componente genitore
    Box (modifier= modifier){
        Button(
            onClick = { expandedState.value = !expandedState.value },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .background(color = colorResource(id = selectedColor), RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    val position = coordinates.positionInWindow()
                    buttonPosition = IntOffset(0, position.y.toInt())
                    buttonSize = coordinates.size
                }
        ) {
            Text(
                selectedOption,
                fontSize = 10.sp,
                color = Color.White,
                fontFamily = pokemonPixelFont,
            )
        }

        if (expandedState.value) {
            val offsetY = if (isLandscape) {
                buttonPosition.y + buttonSize.height
            } else {
                buttonSize.height
            }
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, offsetY),
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .width( maxOf(
                            with(LocalDensity.current) { buttonSize.width.toDp() },
                            200.dp))
                        .height(200.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
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
                                        ) {
                                            if (option != "any") {
                                                Image(
                                                    painterResource(
                                                        id = PokemonTypeConverter().toPokemonType(
                                                            option
                                                        )!!.icon
                                                    ), "icon", modifier = Modifier.height(25.dp)
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                            }
                                            Text(
                                                option,
                                                fontSize = 12.sp, fontFamily = pokemonPixelFont
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedOption = option
                                        if (selectedOption == "any") selectedColor =
                                            R.color.light_pokemon_blue
                                        else selectedColor =
                                            PokemonTypeConverter().toPokemonType(option)!!.backgroundTextColor
                                        onOptionSelected(option)
                                        expandedState.value = false
                                    })
                            }
                        }
                    }
                }
            }
        }
    }


@Composable
fun TopBar(
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
        verticalArrangement = Arrangement.Top) {

        if (pokemon.name.length > 12) Spacer(modifier = Modifier.height(10.dp))
        else Spacer(modifier = Modifier.height(20.dp))
        Text(pokemon.name, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .background(Color.White.copy(0.6f), RoundedCornerShape(10))
            ) {}

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemon.getAnimatedImageUrl())
                    .decoderFactory(ImageDecoderDecoder.Factory())
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
            )
        }
        if (pokemon.type1 !== null) {
            Spacer(modifier = Modifier.height(20.dp))
            TypeRow(type = pokemon.type1)
        }
        if (pokemon.type2 !== null) {
            Spacer(modifier = Modifier.height(20.dp))
            TypeRow(type = pokemon.type2)
        }
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
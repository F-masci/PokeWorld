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
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.pokemonPixelFont
import it.fale.pokeworld.view.TypeRow
import it.fale.pokeworld.viewmodel.PokemonListViewModel
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


    // Funzione per aprire il drawer
    val openDrawer = {
        scope.launch { drawerState.open() }
    }

    // Funzione per chiudere il drawer
    val closeDrawer = {
        scope.launch { drawerState.close() }
    }
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                isDarkTheme = isDarkTheme,
                onItemClick = { selectedItem ->
                    closeDrawer()
                },
                onThemeToggle = { newTheme ->
                    isDarkTheme = newTheme // Aggiorna il tema
                }
            )
        },
        content = {
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
                        SearchBar(
                            pokemonListViewModel = pokemonListViewModel
                        ) { name, type1, type2 ->
                            pokemonListViewModel.filterPokemon(name, type1, type2)
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
                                        .border(2.dp, Color.Gray, RoundedCornerShape(10))
                                        .background(
                                            if (pokemon.type1 != null) colorResource(id = pokemon.type1.backgroundColor)
                                            else Color.Magenta,
                                            RoundedCornerShape(10)
                                        )
                                        .height(250.dp)
                                        .width(250.dp),
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
    )
}

@Composable
fun DrawerContent(
    isDarkTheme: Boolean,
    onItemClick: (String) -> Unit,
    onThemeToggle: (Boolean) -> Unit // Callback per cambiare tema
) {
    var isSwitchOn by remember { mutableStateOf(isDarkTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color.DarkGray else Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Theme",
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemClick("Home") },
                fontSize = 20.sp
            )
            SwitchButton(isLightMode = isSwitchOn) {
                isSwitchOn = it
                onThemeToggle(it) // Chiama la funzione di callback per aggiornare il tema
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Language",
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemClick("Home") },
                fontSize = 20.sp
            )
            ChoiceLanguageMenu(
                initialText = "English",
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = { selectedOption ->
                    // Gestisci l'opzione selezionata qui
                },
                options = listOf("English", "Italiano")
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Alpha Version Database v1.0",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onItemClick("") },
            fontSize = 16.sp
        )
    }
}




@Composable
fun SwitchButton(isLightMode: Boolean, onSwitchChange: (Boolean) -> Unit) {
    /*Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // Centra tutto il contenuto orizzontalmente
    ) {*/
        Button(
            onClick = { onSwitchChange(!isLightMode) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLightMode) Color.LightGray else Color.DarkGray
            ),
            modifier = Modifier
                .background(
                    if (isLightMode) Color.LightGray else Color.DarkGray,
                    RoundedCornerShape(60)
                )
            //.width(110.dp) con questa larghezza raggiungo l'uguaglianza dei due bottoni non so se mi piace
        ) {
            Text(
                text = if (isLightMode) "Light" else "Dark",
                color = if (isLightMode) Color.Black else Color.White,
                fontSize = 14.sp
            )
        }
    //}
}




@Composable
fun SearchBar(
    pokemonListViewModel: PokemonListViewModel, // Aggiungi il parametro qui
    filter: (String?, PokemonType?, PokemonType?) -> Unit) {
    var query by remember { mutableStateOf("") }
    var selectedType1 by remember { mutableStateOf<PokemonType?>(null) }
    var selectedType2 by remember { mutableStateOf<PokemonType?>(null) }

    // Stati per i testi visualizzati nei menu a tendina
    var type1Text by remember { mutableStateOf("Select Type 1") }
    var type2Text by remember { mutableStateOf("Select Type 2") }

    // Funzione per selezionare casualmente i tipi di Pokémon e riprovare finché non viene trovata una combinazione valida
    fun selectRandomTypesWithRetry() {
        // Ottieni una coppia di tipi di Pokémon casuali
        val (randomType1, randomType2) = pokemonListViewModel.randomFilters()

        // Aggiorna i tipi selezionati
        selectedType1 = randomType1
        selectedType2 = randomType2

        // Aggiorna i testi mostrati
        type1Text = selectedType1?.type ?: "Select Type 1"
        type2Text = selectedType2?.type ?: "Select Type 2"

        // Applica il filtro con i nuovi tipi selezionati
        pokemonListViewModel.filterPokemon(null, selectedType1, selectedType2)
    }

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
                    pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2)
                },
                placeholder = { Text("Search...") },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White)
                    .heightIn(min = 56.dp, max = 56.dp),
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
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {

            ChoiceTypeMenu(
                text = type1Text,
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = { selectedOption ->
                    selectedType1 = if (selectedOption == "any") null else PokemonType.fromString(selectedOption)
                    type1Text = if (selectedOption == "any") "Select Type 1" else selectedOption // Aggiorna il testo mostrato
                    filter(query, selectedType1, selectedType2)
                },
                options = listOf("any") + (PokemonType.entries.map { it.type })
            )
            ChoiceTypeMenu(
                text = type2Text,
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = { selectedOption ->
                    selectedType2 = if (selectedOption == "any") null else PokemonType.fromString(selectedOption)
                    type2Text = if (selectedOption == "any") "Select Type 2" else selectedOption // Aggiorna il testo mostrato
                    filter(query, selectedType1, selectedType2)
                },
                options = listOf("any")+(PokemonType.entries.map { it.type })
            )
        }

        Row(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            //Pulsante per la scelta casuale
            Button(onClick = {
                selectRandomTypesWithRetry()
                filter(query, selectedType1, selectedType2)
            },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier
                    .background(colorResource(id = R.color.pokemon_yellow), RoundedCornerShape(100))
                    .width(70.dp)
                    .height(70.dp)){
                Image(painterResource(id = R.drawable.random), "random", modifier = Modifier.size(70.dp))
            }

            Spacer(modifier = Modifier.width(20.dp))
            // Bottone per reimpostare i filtri a "any"
            Button(onClick = {
                selectedType1 = null // Resetta il valore del filtro
                selectedType2 = null // Resetta il valore del filtro
                type1Text = "Select Type 1" // Ripristina il testo del tipo 1
                type2Text = "Select Type 2" // Ripristina il testo del tipo 2
                filter(query, selectedType1, selectedType2)
            },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                modifier = Modifier
                    .background(colorResource(id = R.color.pokemon_yellow), RoundedCornerShape(100))
                    .width(70.dp)
                    .height(70.dp)) {
                Image(painterResource(id = R.drawable.reset), "reset", modifier = Modifier.size(80.dp))
            }
        }
    }
}

@Composable
fun ChoiceLanguageMenu(
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
                .background(Color.DarkGray, RoundedCornerShape(60))
                .width(110.dp)//impostato a mano per essere uguale al button di dark-light mode
        ) {
            Text(
                selectedOption,
                fontSize = 14.sp,
                color = Color.White,
                //opto di proposito per uno stile distaccato per evidenziare il fatto che siamo in impostazioni
                //fontFamily = pokemonPixelFont,
            )
        }
        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false },
            modifier = Modifier
                .width(150.dp)
                .height(110.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem({ Text(option,/*color = if (option == "any") Color.Black else Color.Red  ,*/fontSize = 10.sp,fontFamily = pokemonPixelFont) },onClick = {
                    selectedOption = option
                    onOptionSelected(option)
                    expandedState.value = false
                })
            }
        }
    }
}

@Composable
fun ChoiceTypeMenu(
    text: String,
    expandedState: MutableState<Boolean>,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
) {
    var selectedOption by remember { mutableStateOf(PokemonTypeConverter().toPokemonType(text)?.type ?: "any") }
    var selectedColor by remember{ mutableIntStateOf(PokemonTypeConverter().toPokemonType(text)?.backgroundTextColor ?: R.color.light_pokemon_blue) }

    // Il testo visualizzato viene ora gestito dal componente genitore
    Box {
        Button(
            onClick = { expandedState.value = !expandedState.value },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .background(color = colorResource(id = selectedColor), RoundedCornerShape(10.dp))
                .width(180.dp)
        ) {
            //Image(painterResource(id = PokemonTypeConverter().toPokemonType(selectedOption)!!.icon), "icon")
            Text(
                text,  // Mostra il testo passato come parametro
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
                .clickable { onClick() }
                .height(140.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(10))
        )
        if(pokemon.type1 !== null) TypeRow(type = pokemon.type1)
        if(pokemon.type2 !== null) TypeRow(type = pokemon.type2)
    }
}

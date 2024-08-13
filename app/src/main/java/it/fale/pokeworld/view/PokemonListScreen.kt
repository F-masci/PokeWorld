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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


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
            // Contenuto del drawer
            DrawerContent { selectedItem ->
                // Gestisci l'elemento selezionato qui
                closeDrawer() // Chiudi il drawer quando un elemento è selezionato
            }
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
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
                        SearchBar() { name, type1, type2 ->
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
                                    pokemon = pokemon, modifier = Modifier
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
                                    }                   //questa notazione è fatta in modo tale che posso passare comunque un argomento (l'id)
                                )                       //senza che nella MainActivity vado a definire
                            }
                        }

                    )
                }
            }
        }
    )
}

@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    Column {
        Text("Home", modifier = Modifier
            .padding(16.dp)
            .clickable { onItemClick("Home") }, fontSize = 20.sp)
        Text("Profile", modifier = Modifier
            .padding(16.dp)
            .clickable { onItemClick("Profile") }, fontSize = 20.sp)
        Text("Settings", modifier = Modifier
            .padding(16.dp)
            .clickable { onItemClick("Settings") }, fontSize = 20.sp)
        // Aggiungi altri elementi del drawer qui
    }
}


@Composable
fun SearchBar(filter: (String?, PokemonType?, PokemonType?) -> Unit) {
    var query by remember { mutableStateOf("") }
    var selectedType1 by remember { mutableStateOf<PokemonType?>(null) }
    var selectedType2 by remember { mutableStateOf<PokemonType?>(null) }
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
                    filter(newValue, selectedType1, selectedType2)
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
                initialText = "Select Type 1",
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = { selectedOption ->
                    selectedType1 = PokemonType.fromString(selectedOption)
                    filter(query, selectedType1, selectedType2)
                },
                options = listOf("any")+(PokemonType.entries.map { it.type })
            )
            ChoiceTypeMenu(
                initialText = "Select Type 2",
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = { selectedOption ->
                    selectedType2 = PokemonType.fromString(selectedOption)
                    filter(query, selectedType1, selectedType2)
                },
                options = listOf("any")+(PokemonType.entries.map { it.type })
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
                .height(300.dp)
        ) {
//      possibile reset
//            DropdownMenuItem({Text("Reset", color = Color.Red)},onClick = {
//                selectedOption = initialText // Reset del testo del pulsante al valore iniziale
//                onOptionSelected(initialText)
//                expandedState.value = false
//            })
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
                .clickable { onClick() }
                .height(140.dp)
                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(10))
        )
        if(pokemon.type1 !== null) TypeRow(type = pokemon.type1)
        if(pokemon.type2 !== null) TypeRow(type = pokemon.type2)
    }
}

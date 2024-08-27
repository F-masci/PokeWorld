package it.fale.pokeworld.view.list

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.list.TopbarConstants
import it.fale.pokeworld.ui.theme.pokemonPixelFont
import it.fale.pokeworld.ui.theme.themedColorsPalette
import it.fale.pokeworld.ui.theme.themedTypography


/**
 * Composable per la barra di ricerca.
 *
 * @param isSearchBarVisible Indica se la barra di ricerca è visibile.
 * @param query La query corrente.
 * @param selectedType1 Il tipo selezionato 1.
 * @param selectedType2 Il tipo selezionato 2.
 * @param onSearchClicked La funzione da eseguire quando viene cliccato il pulsante di ricerca.
 * @param onSettingsClicked La funzione da eseguire quando viene cliccato il pulsante delle impostazioni.
 * @param filterPokemon La funzione da eseguire per filtrare i Pokémon.
 * @param randomFilters La funzione da eseguire per ottenere filtri casuali.
 * @param favoritePokemon Il Pokémon preferito.
 * @param navController Il controller di navigazione.
 */
@Composable
fun TopBar(
    isSearchBarVisible: Boolean,
    query: String?,
    selectedType1: PokemonType?,
    selectedType2: PokemonType?,
    onSearchClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    filterPokemon: (String?, PokemonType?, PokemonType?) -> Unit,
    randomFilters: () -> Pair<PokemonType, PokemonType>,
    favoritePokemon: PokemonEntity?,
    navController: NavController
){

    // Variabile per memorizzare la query corrente
    var _query: String = query ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopbarConstants.topBarHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onSettingsClicked,
            modifier = Modifier.size(TopbarConstants.buttonSize)
        ) {
            Image(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings",
            )
        }
        IconButton(onClick = onSearchClicked,
            modifier = Modifier.size(TopbarConstants.buttonSize)) {
            Image(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search",
            )
        }
        Spacer(Modifier.width(TopbarConstants.spacerHeight))
        AsyncImage(
            model = R.drawable.logo,
            contentDescription = "Logo",
            modifier = Modifier.height(TopbarConstants.logoHeight)
        )
        Spacer(Modifier.width(TopbarConstants.spacerHeight))
        FavePokemon(favoritePokemon, navController)
    }
    if (isSearchBarVisible) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TopbarConstants.searchBarPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = _query,
                    onValueChange = { newValue ->
                        filterPokemon(newValue, selectedType1, selectedType2)
                    },
                    placeholder = { Text(stringResource(R.string.search)) },
                    modifier = Modifier
                        .heightIn(
                            min = TopbarConstants.searchBarHeight,
                            max = TopbarConstants.searchBarHeight
                        )
                        .weight(1f),
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = MaterialTheme.themedColorsPalette.mainBlue,
                        unfocusedPlaceholderColor = MaterialTheme.themedColorsPalette.mainBlue,
                        unfocusedTextColor = MaterialTheme.themedColorsPalette.mainBlue,
                        focusedIndicatorColor = MaterialTheme.themedColorsPalette.mainLightBlue,
                        focusedContainerColor = MaterialTheme.themedColorsPalette.mainLightBlue,
                        focusedTextColor = MaterialTheme.themedColorsPalette.mainLightYellow,
                        focusedPlaceholderColor = MaterialTheme.themedColorsPalette.mainLightYellow,
                        unfocusedContainerColor = MaterialTheme.themedColorsPalette.mainYellow
                    ),
                    shape = RoundedCornerShape(topStart = TopbarConstants.searchBarRadius, topEnd = TopbarConstants.searchBarRadius)
                )
                Spacer(modifier = Modifier.width(TopbarConstants.spacerWidth))

                Button(onClick = {
                    filterPokemon(null, null, null)
                },
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    modifier = Modifier
                        .background(
                            MaterialTheme.themedColorsPalette.mainYellow,
                            RoundedCornerShape(TopbarConstants.buttonRadius)
                        )
                        .width(TopbarConstants.buttonWidth)
                        .height(TopbarConstants.buttonHeight)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.reset),
                        contentDescription = "reset",
                        tint = MaterialTheme.themedColorsPalette.mainBlue,
                        modifier = Modifier.size(TopbarConstants.buttonIconSize)
                    )
                }
            }
            Spacer(modifier = Modifier.height(TopbarConstants.secondarySpacerHeight))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                // Filtro per il tipo 1
                ChoiceTypeMenu(
                    type = selectedType1,
                    expandedState = remember { mutableStateOf(false) },
                    onOptionSelected = { selectedOption ->
                        filterPokemon(query, PokemonType.fromType(selectedOption), selectedType2)
                    },
                    options = listOf(Pair<PokemonType?, String>(null, stringResource(id = R.string.any))) + (PokemonType.entries.map { Pair<PokemonType?, String>(it, stringResource(id = it.string)) }),
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(TopbarConstants.spacerWidth))

                // Filtro per il tipo 2
                ChoiceTypeMenu(
                    type = selectedType2,
                    expandedState = remember { mutableStateOf(false) },
                    onOptionSelected = { selectedOption ->
                        filterPokemon(query, selectedType1, PokemonType.fromType(selectedOption))
                    },
                    options = listOf(Pair<PokemonType?, String>(null, stringResource(id = R.string.any))) + (PokemonType.entries.map { Pair<PokemonType?, String>(it, stringResource(id = it.string)) }),
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(TopbarConstants.spacerWidth))

                //Pulsante per la scelta casuale
                Button(onClick = {
                    val (type1, type2) = randomFilters()
                    filterPokemon(query, type1, type2)
                },
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    modifier = Modifier
                        .background(
                            MaterialTheme.themedColorsPalette.mainYellow,
                            RoundedCornerShape(TopbarConstants.buttonRadius)
                        )
                        .width(TopbarConstants.buttonWidth)
                        .height(TopbarConstants.buttonHeight)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.random),
                        contentDescription = "random",
                        tint = MaterialTheme.themedColorsPalette.mainBlue,
                        modifier = Modifier.size(TopbarConstants.buttonIconSize)
                    )
                }
            }
        }
    }
}

/**
 * Composable per il Pokémon preferito.
 *
 * @param pokemon Il Pokémon preferito.
 * @param navController Il controller di navigazione.
 */
@Composable
fun FavePokemon(
    pokemon: PokemonEntity?,
    navController: NavController
){

    var showDialog by rememberSaveable { mutableStateOf(false) }

    if(pokemon == null) {
        Spacer(Modifier.width(TopbarConstants.favoriteSpacerWidth))
        Image(
            painter = painterResource(id = R.drawable.yellowstar),
            contentDescription = "star",
            Modifier
                .height(TopbarConstants.favoriteStarIconHeight)
                .clickable {
                    showDialog = true
                })
    }
    else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .clickable { navController.navigate("pokemon_details_screen/${pokemon.id}") }){
            AsyncImage(
                model = pokemon.spriteDefault,
                contentDescription = "Favorite Pokemon",
                modifier = Modifier.height(TopbarConstants.favoriteIconHeight)
            )
            Image(
                painter = painterResource(id = R.drawable.yellowstar),
                contentDescription = "star",
                Modifier.height(TopbarConstants.favoriteStarIconHeight)
            )
        }
    }
    if(showDialog)
        AlertDialog (
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            text = {
                Text(stringResource(id = R.string.no_favorite_disclaimer))
            }
        )
}

/**
 * Composable per il menu di selezione di tipo.
 *
 * @param type Il tipo selezionato.
 * @param expandedState Lo stato di estensione del menu.
 * @param onOptionSelected La funzione da eseguire quando viene selezionata una opzione.
 * @param options Le opzioni del menu.
 * @param modifier I modificatori da applicare.
 */
@Composable
fun ChoiceTypeMenu(
    type: PokemonType?,
    expandedState: MutableState<Boolean>,
    onOptionSelected: (String) -> Unit,
    options: List<Pair<PokemonType?, String>>,
    modifier: Modifier
) {

    // Variabili per la gestione del menu
    var selectedOption = if(type != null) stringResource(type.string) else stringResource(R.string.any)
    var selectedColor = type?.getBackgroundTextColor?.invoke() ?: MaterialTheme.themedColorsPalette.mainBlue
    var buttonPosition by remember { mutableStateOf(IntOffset(0, 0)) }
    var buttonSize by remember { mutableStateOf(IntSize.Zero) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Il testo visualizzato viene gestito dal componente genitore
    Box (modifier = modifier){
        Button(
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            onClick = { expandedState.value = !expandedState.value },
            modifier = Modifier
                .background(
                    color = selectedColor,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    val position = coordinates.positionInWindow()
                    buttonPosition = IntOffset(0, position.y.toInt())
                    buttonSize = coordinates.size
                }
        ) {
            Text(
                selectedOption,
                style = MaterialTheme.themedTypography.topbarFilterTypeLabel,
                color = MaterialTheme.themedColorsPalette.topbarFilterTypeTextColor,
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
                    shape = RoundedCornerShape(TopbarConstants.choiceMenuCornerShape),
                    modifier = Modifier
                        .width(
                            maxOf(
                                with(LocalDensity.current) { buttonSize.width.toDp() },
                                TopbarConstants.choiceMenuMaxWidth
                            )
                        )
                        .height(TopbarConstants.choiceMenuHeight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        options.forEach { (t, text) ->

                            val backgroundColor = t?.getBackgroundColor?.invoke() ?: Color.White
                            val backgroundTextColor = t?.getBackgroundTextColor?.invoke() ?: Color.White

                            DropdownMenuItem(
                                {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(TopbarConstants.choiceMenuButtonHeight)
                                            .background(
                                                backgroundColor,
                                                RoundedCornerShape(TopbarConstants.choiceMenuButtonRadius)
                                            ),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        if (t != null) {
                                            Image(
                                                painter = painterResource(id = t.icon),
                                                contentDescription = "icon",
                                                modifier = Modifier
                                                    .height(TopbarConstants.choiceMenuIconOptionsHeight)
                                            )
                                            Spacer(modifier = Modifier.width(TopbarConstants.spacerWidth))
                                        }
                                        Text(
                                            text = text,
                                            style = MaterialTheme.themedTypography.topbarFilterTypeOptionsLabel,
                                            color = MaterialTheme.themedColorsPalette.topbarFilterTypeTextColor
                                        )
                                    }
                                },
                                onClick = {
                                    selectedOption = text
                                    selectedColor = backgroundTextColor
                                    onOptionSelected(t?.type ?: "any")
                                    expandedState.value = false
                                })
                        }
                    }
                }
            }
        }
    }
}
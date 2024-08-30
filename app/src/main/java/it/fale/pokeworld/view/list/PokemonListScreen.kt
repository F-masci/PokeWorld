package it.fale.pokeworld.view.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.list.PokemonListConstants
import it.fale.pokeworld.ui.theme.themedColorsPalette
import it.fale.pokeworld.utils.Language
import it.fale.pokeworld.viewmodel.PokemonListViewModel
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Composable per la lista di Pokémon.
 *
 * @param onPokemonClicked La funzione da eseguire quando viene cliccato un Pokémon.
 * @param pokemonListViewModel Il ViewModel per la lista di Pokémon.
 * @param isDarkTheme Il flag che indica se il tema è scuro.
 * @param onThemeToggle La funzione da eseguire quando cambia lo stato del tema.
 * @param language La lingua corrente.
 * @param onLanguageChange La funzione da eseguire quando cambia la lingua.
 */
@Composable
fun PokemonListScreen(
    onPokemonClicked: (Int) -> Unit,
    pokemonListViewModel: PokemonListViewModel,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    onThemeToggle: (Boolean) -> Unit = {},
    language: Language = Language.fromCode(Locale.getDefault().language),
    onLanguageChange: (Language) -> Unit = {}
) {

    // Ottiene lo stato della lista di Pokémon dal ViewModel e del pokemon preferito
    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()
    val favoritePokemon = pokemonListViewModel.favoritePokemon.collectAsStateWithLifecycle()

    var isSearchBarVisible by rememberSaveable { mutableStateOf(false) }

    // Variabile per memorizzare lo stato della LazyVerticalGrid, che permette di controllare la posizione di scroll e del drawer
    val scope = rememberCoroutineScope()
    val pokemonListState = rememberLazyGridState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Variabili per i filtri
    var query by rememberSaveable { mutableStateOf<String?>("") }
    var selectedType1 by rememberSaveable { mutableStateOf<PokemonType?>(null) }
    var selectedType2 by rememberSaveable { mutableStateOf<PokemonType?>(null) }

    // Controllo che la lista dei pokemon sia stata effettivamente caricata
    if(pokemonList.value.isEmpty() && (query == null || query!!.isBlank()) && selectedType1 == null && selectedType2 == null)
        SplashScreen()
    else
        // Drawer per le impostazioni
        SettingsDrawer(
            drawerState = drawerState,
            isDarkTheme = isDarkTheme,
            language = language.text,
            onThemeToggle = { newTheme ->
                // Salva lo stato del tema
                pokemonListViewModel.saveDarkModePreference(newTheme)
                // Aggiorna lo stato della UI
                onThemeToggle(newTheme)
            },
            onLanguageChange = { newLanguage ->
                // Salva lo stato della lingua
                pokemonListViewModel.saveLanguagePreference(newLanguage)
                // Aggiorna lo stato della UI
                onLanguageChange(newLanguage)
                // Ricarica la lista di Pokémon resettando i filtri
                query = ""
                selectedType1 = null // Resetta il valore del filtro
                selectedType2 = null // Resetta il valore del filtro
                pokemonListViewModel.filterPokemon(null, null, null)
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.themedColorsPalette.mainListBackgroundColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PokemonListConstants.mainListPadding)
                ) {
                    TopBar(
                        isSearchBarVisible = isSearchBarVisible,
                        query = query,
                        selectedType1 = selectedType1,
                        selectedType2 = selectedType2,
                        onSearchClicked = {
                            isSearchBarVisible = !isSearchBarVisible
                        },
                        onSettingsClicked = {
                            scope.launch { drawerState.open() }
                        },
                        filterPokemon = { newQuery, newType1, newType2 ->
                            query = newQuery
                            selectedType1 = newType1
                            selectedType2 = newType2
                            pokemonListViewModel.filterPokemon(query, selectedType1, selectedType2) },
                        randomFilters = {
                            pokemonListViewModel.randomFilters()
                        },
                        favoritePokemon = favoritePokemon.value,
                        onFavoritePokemonClicked = onPokemonClicked
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight(PokemonListConstants.MAX_MAIN_BOX_SIZE)
                            .fillMaxWidth(PokemonListConstants.MAX_MAIN_BOX_SIZE)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(PokemonListConstants.cellAdaptiveSize),
                            horizontalArrangement = Arrangement.spacedBy(PokemonListConstants.cellArrangement),
                            verticalArrangement = Arrangement.spacedBy(PokemonListConstants.cellArrangement),
                            state = pokemonListState,
                            content = {
                                items(pokemonList.value) { pokemon ->
                                    PokemonCard(
                                        pokemon = pokemon,
                                        modifier = Modifier
                                            .padding(PokemonListConstants.cardPadding)
                                            .border(
                                                PokemonListConstants.cardBorderRadius,
                                                MaterialTheme.themedColorsPalette.mainListCardBorder,
                                                RoundedCornerShape(PokemonListConstants.CARD_BACKGROUND_RADIUS))
                                            .height(PokemonListConstants.cardHeight)
                                            .width(PokemonListConstants.cardWidth)
                                            .background(
                                                pokemon.type1!!.getBackgroundColor(),
                                                RoundedCornerShape(PokemonListConstants.CARD_BACKGROUND_RADIUS)
                                            ),
                                        onClick = {
                                            onPokemonClicked(pokemon.id)
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
                                .padding(PokemonListConstants.buttonUpArrowPadding)
                                .background(
                                    MaterialTheme.themedColorsPalette.mainYellow,
                                    RoundedCornerShape(PokemonListConstants.buttonUpArrowRadius)
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.uparrow),
                                contentDescription = "Up arrow",
                                tint = MaterialTheme.themedColorsPalette.mainBlue
                            )
                        }
                    }
                }
            }
        }
}


package it.fale.pokeworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.repository.PokemonDatabase
import it.fale.pokeworld.repository.PokemonRepository
import it.fale.pokeworld.ui.theme.PokeWorldTheme
import it.fale.pokeworld.view.detail.PokemonDetailsScreen
import it.fale.pokeworld.view.list.PokemonListScreen
import it.fale.pokeworld.viewmodel.ViewModelFactory
import it.fale.pokeworld.repository.FavoritePokemonSharedRepository
import it.fale.pokeworld.repository.PokemonRepositoryImpl
import it.fale.pokeworld.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Classe principale dell'applicazione.
 */
@AndroidEntryPoint
class PokeWorld : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        /* --- Inizializzazione --- */

        UserPreferencesRepository.initialize(this)

        val pokemonRepository: PokemonRepository = PokemonRepositoryImpl(PokemonDatabase.getInstance(applicationContext).pokemonDao())
        loadFavoritePokemon(pokemonRepository)

        val factory = ViewModelFactory(pokemonRepository)

        /* ------------------------ */

        setContent {

            val navController = rememberNavController()
            val isSystemInDarkTheme = isSystemInDarkTheme()

            var isDarkTheme by rememberSaveable { mutableStateOf(UserPreferencesRepository.getDarkModePreference(isSystemInDarkTheme)) }
            var language by rememberSaveable { mutableStateOf(UserPreferencesRepository.getLanguagePreference()) }

            PokeWorldTheme(
                darkTheme = isDarkTheme,
                languageCode = language.code,
                context = applicationContext
            ) {

                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen",
                ) {
                    composable("pokemon_list_screen") {
                        
                        PokemonListScreen(
                            onPokemonClicked = { navController.navigate("pokemon_details_screen/$it") },
                            pokemonListViewModel = viewModel(factory = factory),
                            isDarkTheme = isDarkTheme,
                            onThemeToggle = { isDark ->
                                AppCompatDelegate.setDefaultNightMode(if(isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                                isDarkTheme = isDark
                            },
                            language = language,
                            onLanguageChange = { newLanguage ->
                                language = newLanguage
                            }
                        )

                    }
                    composable(
                        route = "pokemon_details_screen/{pokemonId}",
                        arguments = listOf(navArgument("pokemonId") {
                            type = NavType.IntType
                        })
                    ) {
                        // ID del pokemon passato come argomento della route
                        val pokemonId = it.arguments!!.getInt("pokemonId")
                        PokemonDetailsScreen(
                            pokemonDetailViewModel = viewModel(factory = factory),
                            pokemonId = pokemonId
                        )
                    }
                }
            }
        }

    }

    /**
     * Carica il pokemon preferito salvato nelle SharedPreferences
     * nel repository condiviso per essere utilizzato da più ViewModel.
     *
     * @param pokemonRepository Repository per le operazioni di database.
     */
    private fun loadFavoritePokemon(pokemonRepository: PokemonRepository) {

        // Carica l'id del pokemon preferito
        val favoriteId: Int? = UserPreferencesRepository.getFavoritePokemonId()

        // Imposta il pokemon preferito nel repository condiviso
        CoroutineScope(Dispatchers.IO).launch {
            val favPokemon: PokemonEntity? = pokemonRepository.retrievePokemon(favoriteId ?: 0)
            FavoritePokemonSharedRepository.updateFavoritePokemon(favPokemon)
        }

    }

}
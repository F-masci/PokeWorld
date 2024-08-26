package it.fale.pokeworld

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.repository.PokemonDatabase
import it.fale.pokeworld.entity.repository.PokemonRepository
import it.fale.pokeworld.ui.theme.PokeWorldTheme
import it.fale.pokeworld.utils.FAVOURITE_KEY
import it.fale.pokeworld.utils.LANGUAGE_KEY
import it.fale.pokeworld.utils.PREFERENCES_NAME
import it.fale.pokeworld.view.PokemonDetailsScreen
import it.fale.pokeworld.view.list.PokemonListScreen
import it.fale.pokeworld.viewmodel.ViewModelFactory
import it.fale.pokeworld.viewmodel.shared.FavoritePokemonSharedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Classe principale dell'applicazione.
 */
@AndroidEntryPoint
class PokeWorld : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val favoriteId = sharedPreferences.getInt(FAVOURITE_KEY, -1)

        val repository = PokemonRepository(PokemonDatabase.getInstance(context).pokemonDao())
        val sharedRepository = FavoritePokemonSharedRepository()
        CoroutineScope(Dispatchers.IO).launch {
            val favPokemon: PokemonEntity? = repository.retrievePokemon(favoriteId)
            sharedRepository.updateFavoritePokemon(favPokemon)
        }

        val factory = ViewModelFactory(repository, sharedRepository)

        setContent{

            val navController = rememberNavController()

            PokeWorldTheme {

                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen",
                ) {
                    composable("pokemon_list_screen") {

                        PokemonListScreen(
                            navController = navController,
                            pokemonListViewModel = viewModel(factory = factory)
                        )

                    }
                    composable(
                        route = "pokemon_details_screen/{pokemonId}", //pokemonId è un parametro dinamico, che inserisco solamente quando dalla listScreen clicco sul pokemon interessato
                        arguments = listOf(navArgument("pokemonId") {
                            type = NavType.IntType
                        })
                    ) {
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

    override fun attachBaseContext(base: Context) {
        val prefs = base.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val language = prefs.getString(LANGUAGE_KEY, Locale.getDefault().language)!!

        val config = Configuration(base.resources.configuration)
        config.setLocale(Locale(language))

        super.attachBaseContext(ContextWrapper(base.createConfigurationContext(config)))
    }

}
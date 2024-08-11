package it.fale.pokeworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import it.fale.pokeworld.entity.repository.PokemonDatabase
import it.fale.pokeworld.entity.repository.PokemonRepository
import it.fale.pokeworld.ui.theme.PokeWorldTheme
import it.fale.pokeworld.view.DetailsScreen
import it.fale.pokeworld.view.WelcomeScreen
import it.fale.pokeworld.viewmodel.PokemonListViewModel


//Questa è la nuova classe di avvio che gestisce la navigazione
//L'unica cosa che voglio capire e che non mi convince è come devo gestire la repository,
// se creare la variabile qui o direttamente nella schermata della lista
@AndroidEntryPoint
class MainActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            val repository = PokemonRepository(PokemonDatabase.getInstance(LocalContext.current).pokemonDao())
            val pokemonListViewModel = PokemonListViewModel(repository)
            PokeWorldTheme {
                val navController= rememberNavController()
                NavHost(
                    navController=navController,
                    startDestination= "welcome_screen"
                ){
                    composable("pokemon_list_screen"){
                            PokemonList(navController,pokemonListViewModel)
                    }
                    composable("welcome_screen"){
                        WelcomeScreen(navController = navController)
                    }
                    composable(
                        route = "details_screen/{pokemonId}", //pokemonId è un parametro dinamico, che inserisco solamente quando dalla listScreen clicco sul pokemon interessato
                        arguments = listOf(navArgument("pokemonId") {
                            type = NavType.IntType
                        })
                    ) { backStackEntry ->   //qui vado ad estrarre il valore dell'id del pokemon grazie a backStackEntry
                        val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: -1
                        DetailsScreen(navController = navController, pokemonId = pokemonId)
                    }
                }//questo metodo ci può stare per estrarre i dati, se vi viene in mente qualcosa di meglio , fatemi sapere
            }
        }
    }
}
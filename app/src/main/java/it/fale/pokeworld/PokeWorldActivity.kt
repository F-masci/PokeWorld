package it.fale.pokeworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.repository.PokemonDatabase
import it.fale.pokeworld.entity.repository.PokemonRepository
import it.fale.pokeworld.ui.theme.PokeWorldTheme
import it.fale.pokeworld.viewmodel.PokemonListViewModel

class PokeWorldActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val repository = PokemonRepository(PokemonDatabase.getInstance(LocalContext.current).pokemonDao())
            val pokemonListViewModel = PokemonListViewModel(repository)

            PokeWorldTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokemonList(pokemonListViewModel)
                }
            }
        }
    }
}

@Composable
fun PokemonList(pokemonListViewModel: PokemonListViewModel) {

    val pokemonList = pokemonListViewModel.pokemonList.collectAsStateWithLifecycle()

    LazyColumn{
        items(pokemonList.value) { pokemon ->
            PokemonDetail(pokemon = pokemon, modifier = Modifier.padding(16.dp))
        }
    }
}

@Composable
fun PokemonDetail(pokemon: PokemonEntity, modifier: Modifier) {

    var txt = pokemon.name
    if(pokemon.type1 !== null) txt += " - " + stringResource(id = pokemon.type1.resource)
    if(pokemon.type2 !== null) txt += " - " + stringResource(id = pokemon.type2.resource)
    txt += ": "
    pokemon.abilities.forEach {ability ->
        txt += ability.getLocaleName() + " (" + ability.getLocaleEffect() + ")"
    }

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
        pokemon.abilities.forEach {ability ->
            Row {
                Text(ability.getLocaleName() + " (" + ability.getLocaleEffect() + ")")
            }
        }
    }

}
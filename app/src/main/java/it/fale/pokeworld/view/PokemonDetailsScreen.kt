package it.fale.pokeworld.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import it.fale.pokeworld.PokemonCard
import it.fale.pokeworld.getBackgroundColorForType
import it.fale.pokeworld.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailsScreen (
    navController: NavController,
    pokemonDetailViewModel: PokemonDetailViewModel
)
{

    val pokemon = pokemonDetailViewModel.pokemon.collectAsStateWithLifecycle().value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            PokemonCard(
                pokemon = pokemon,
                modifier = Modifier
                    .padding(20.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(10))
                    .background(
                        if (pokemon.type1 != null) getBackgroundColorForType(type = pokemon.type1) else Color.Magenta,
                        RoundedCornerShape(10)
                    )
                    .height(250.dp)
                    .width(250.dp),
                onClick = {}
            )
        }
    }

}

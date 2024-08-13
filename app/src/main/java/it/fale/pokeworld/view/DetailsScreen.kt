package it.fale.pokeworld.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.entity.PokemonType


@Composable
fun DetailsScreen(
    pokemonId: Int
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
        ) {
            Text(text = "Pokemon ID: $pokemonId")
        }
    }
}

@Composable
fun TypeRow(type: PokemonType) {

    Row (modifier = Modifier
        .background(
            colorResource(type.backgroundTextColor)
                .copy(alpha = 0.8f),
            RoundedCornerShape(30)
        )
        .width(135.dp)
        .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Image(
            painterResource(id = type.icon), "type_icon",
            modifier = Modifier
                .padding(2.dp)
                .height(20.dp))
        Text(stringResource(id = type.string), fontSize = 12.sp)
    }
}
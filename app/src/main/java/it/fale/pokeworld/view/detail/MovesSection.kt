package it.fale.pokeworld.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.WhiteDetails

/**
 * Composable per la sezione relativa alle mosse del Pokemon.
 *
 * @param pokemon Il Pokemon di cui mostrare le mosse.
 */
@Composable
fun MovesSection(pokemon: PokemonEntity){
    Text(
        text = stringResource(R.string.moves),
        fontSize = 14.sp,
        modifier = Modifier.padding(15.dp)
    )
    pokemon.moves.forEach { move ->
        MoveItem(
            moveName = move.getLocaleName(),
            moveDescription = move.getLocaleDescription(),
            accuracy = move.accuracy,
            effectChance = move.effectChance,
            power = move.power,
            priority = move.priority,
            type = pokemon.type1!!
        )
    }
}

/**
 * Composable per mostrare una mossa.
 *
 * @param moveName Il nome della mossa.
 * @param moveDescription La descrizione della mossa.
 * @param accuracy La precisione della mossa.
 * @param effectChance La probabilità di effetto della mossa.
 * @param power La potenza della mossa.
 * @param priority La priorità della mossa.
 * @param type Il tipo del Pokemon di cui stiamo mostrando la mossa.
 */
@Composable
fun MoveItem(
    moveName: String,
    moveDescription: String,
    accuracy: Int?,
    effectChance: Int?,
    power: Int?,
    priority: Int?,
    type: PokemonType
) {
    var isExpanded by remember { mutableStateOf(false) }
    val hasDescription = moveDescription.isNotEmpty()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                type
                    .getBackgroundTextColor()
                    .copy(alpha = 0.8f),
                RoundedCornerShape(10.dp)
            )
            .padding(15.dp)
            .clickable { isExpanded = !isExpanded }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = moveName,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.priority) + ": ${priority ?: "-"}",
                fontSize = 8.sp,
                modifier = Modifier
                    .background(Color.White.copy(0.8f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = Color.Black.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (hasDescription) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.description),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Arrow(isExpanded = isExpanded)
            }

        }
        if (hasDescription && isExpanded) {
            Text(
                text = moveDescription,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(0.6f), RoundedCornerShape(5.dp))
                    .padding(4.dp)

            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ValueMoves(stringResource(R.string.accuracy), accuracy)
            ValueMoves(stringResource(R.string.effect_chance), effectChance)
            ValueMoves(stringResource(R.string.power), power)
        }

    }
}

@Composable
fun ValueMoves(title: String, value: Int?){
    Text(
        text = title+":\n ${value ?: "-"}",
        fontSize = 8.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(WhiteDetails, RoundedCornerShape(10.dp))
            .padding(8.dp)


    )
}

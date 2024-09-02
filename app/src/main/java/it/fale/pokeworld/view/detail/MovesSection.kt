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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.WhiteDetails
import it.fale.pokeworld.ui.theme.detail.MoveItemConstants
import it.fale.pokeworld.ui.theme.themedTypography

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
            .padding(MoveItemConstants.itemOuterPadding)
            .background(
                type.getBackgroundTextColor().copy(alpha = MoveItemConstants.ITEM_BACKGROUND_ALPHA),
                RoundedCornerShape(MoveItemConstants.itemRoundedCornerShape)
            )
            .padding(MoveItemConstants.itemPadding)
            .clickable { isExpanded = !isExpanded }
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = moveName,
                style = MaterialTheme.themedTypography.itemTextStyle,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
            Text(
                text = stringResource(R.string.priority) + ": ${priority ?: "-"}",
                fontSize = 8.sp, // Esempio di stile non incluso nelle costanti
                modifier = Modifier
                    .background(Color.White.copy(MoveItemConstants.PRIORITY_BACKGROUND_ALPHA),
                        RoundedCornerShape(MoveItemConstants.priorityRoundedCornerShape)
                    )
                    .padding(
                        horizontal = MoveItemConstants.priorityPadding,
                        vertical = MoveItemConstants.priorityVerticalPadding
                    ),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(MoveItemConstants.spacerHeight))
        Divider(
            color = Color.Black.copy(alpha = MoveItemConstants.DIVIDER_ALPHA),
            thickness = MoveItemConstants.dividerThickness,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MoveItemConstants.spacerHeight))
        if (hasDescription) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.themedTypography.itemTextStyle,
                    modifier = Modifier.padding(horizontal = MoveItemConstants.descriptionPadding)
                )
                Arrow(isExpanded = isExpanded)
            }

        }
        if (hasDescription && isExpanded) {
            Text(
                text = moveDescription,
                fontSize = 9.sp, // Esempio di stile non incluso nelle costanti
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(MoveItemConstants.EXPANDED_DESCRIPTION_BACKGROUND_ALPHA),
                        RoundedCornerShape(MoveItemConstants.expandedDescriptionRoundedCornerShape)
                    )
                    .padding(MoveItemConstants.expandedDescriptionPadding)

            )
        }
        Spacer(modifier = Modifier.height(MoveItemConstants.spacerHeight))
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
        fontSize = 6.sp, // Esempio di stile non incluso nelle costanti
        textAlign = TextAlign.Center,
        modifier = Modifier
            .background(WhiteDetails, RoundedCornerShape(MoveItemConstants.valueRoundedCornerShape))
            .padding(MoveItemConstants.valuePadding)


    )
}
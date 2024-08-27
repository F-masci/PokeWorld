package it.fale.pokeworld.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.ui.theme.WhiteDetails
import it.fale.pokeworld.ui.theme.detail.ItemCardConstants
import it.fale.pokeworld.ui.theme.detail.ItemSectionConstants

/**
 * Composable per la sezione relativa agli oggetti del Pokemon.
 *
 * @param pokemon Il Pokemon di cui mostrare gli oggetti.
 */
@Composable
fun ItemSection(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ItemSectionConstants.sectionPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.items),
            fontSize = 14.sp,
            modifier = Modifier.padding(ItemSectionConstants.sectionPadding)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(ItemSectionConstants.cardSpacing),
            modifier = Modifier.fillMaxWidth()
        ) {
            pokemon.items.forEach { item ->
                ItemCard(
                    itemName = item.getLocaleName(),
                    itemCost = item.cost,
                    itemEffect = item.getLocaleEffect(),
                    itemSprite = item.sprite,
                    typeColor = pokemon.type1!!.getBackgroundTextColor()
                )
            }
        }
    }
}

/**
 * Composable per mostrare un oggetto.
 *
 * @param itemName Il nome dell'oggetto.
 * @param itemCost Il costo dell'oggetto.
 * @param itemEffect L'effetto che ha l'oggetto.
 * @param itemSprite L'url dell'immagine dell'oggetto.
 * @param typeColor Il primo colore associato al primo tipo del Pokemon di cui si sta mostrando l'oggetto.
 */
@Composable
fun ItemCard(
    itemName: String,
    itemCost: Int,
    itemEffect: String,
    itemSprite: String?,
    typeColor: Color) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ItemCardConstants.itemPadding)
            .border(
                width = ItemCardConstants.borderWidth,
                color = typeColor,
                shape = RoundedCornerShape(ItemCardConstants.roundedCornerShape)
            )
            .background(WhiteDetails, RoundedCornerShape(ItemCardConstants.roundedCornerShape))
            .clickable { isExpanded = !isExpanded }
            .padding(ItemCardConstants.itemPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            itemSprite?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier.size(ItemCardConstants.imageSize)
                )
            }
            Text(
                text = itemName,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = ItemCardConstants.itemNamePaddingTop)
            )
            Arrow(isExpanded)
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(ItemCardConstants.expandedSectionSpacerHeight))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(stringResource(R.string.cost) + ":")
                    }
                    append(" $itemCost")
                },
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ItemCardConstants.textPaddingTop)
            )

            Spacer(modifier = Modifier.height(ItemCardConstants.textPaddingTop))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(stringResource(R.string.effect) + ":")
                    }
                    append(" $itemEffect")
                },
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = ItemCardConstants.textPaddingTop))
        }
    }
}
package it.fale.pokeworld.view.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.ui.theme.list.PokemonCardConstants
import it.fale.pokeworld.ui.theme.themedColorsPalette
import it.fale.pokeworld.ui.theme.themedTypography
import it.fale.pokeworld.view.detail.TypeRow

/**
 * Composable per la card di un Pokémon.
 *
 * @param pokemon Il Pokémon da visualizzare nella card.
 * @param modifier I modificatori da applicare all'elemento.
 * @param onClick La funzione da eseguire quando l'utente clicca sulla card.
 */
@Composable
fun PokemonCard(
    pokemon: PokemonEntity,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        if (pokemon.name.length > 12) Spacer(modifier = Modifier.height(PokemonCardConstants.firstSpacerHeight))
        else Spacer(modifier = Modifier.height(PokemonCardConstants.spacerHeight))
        Text(pokemon.name, style = MaterialTheme.themedTypography.pokemonCardTitle)
        Spacer(modifier = Modifier.height(PokemonCardConstants.spacerHeight))
        Box(
            modifier = Modifier
                .width(PokemonCardConstants.cardWidth)
                .height(PokemonCardConstants.cardHeight),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .width(PokemonCardConstants.cardWidth)
                    .height(PokemonCardConstants.cardHeight)
                    .background(
                        MaterialTheme.themedColorsPalette.pokemonImageBackground,
                        RoundedCornerShape(PokemonCardConstants.pokemonImageBorderRadius)
                    )
            ) {}

            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(pokemon.getAnimatedImageUrl())
                    .decoderFactory(ImageDecoderDecoder.Factory())
                    .build(),
                contentDescription = "Pokemon GIF",
                modifier = Modifier
                    .height(PokemonCardConstants.pokemonImageWidth)
                    .width(PokemonCardConstants.pokemonImageHeight)
            )
        }
        if (pokemon.type1 !== null) {
            Spacer(modifier = Modifier.height(PokemonCardConstants.spacerHeight))
            TypeRow(type = pokemon.type1)
        }
        if (pokemon.type2 !== null) {
            Spacer(modifier = Modifier.height(PokemonCardConstants.spacerHeight))
            TypeRow(type = pokemon.type2)
        }
    }
}
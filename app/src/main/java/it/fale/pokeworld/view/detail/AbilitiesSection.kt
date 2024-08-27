package it.fale.pokeworld.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity

/**
 * Composable per la sezione relativa alle abilità del Pokemon.
 *
 * @param pokemon Il Pokemon di cui mostrare le abilità.
 */
@Composable
fun AbilitiesSection(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.abilities),
            fontSize = 14.sp,
            modifier = Modifier.padding(15.dp)
        )
        pokemon.abilities.forEach { ability ->
            AbilityItem(
                abilityName = ability.getLocaleName(LocalContext.current),
                abilityDescription = ability.getLocaleEffect(LocalContext.current),
                typeColor = pokemon.type1?.getBackgroundTextColor!!(),
                backgroundColor = pokemon.type1?.getBackgroundColor!!()
            )
        }
    }
}

/**
 * Composable per mostrare una abilità.
 *
 * @param abilityName Il nome dell'abilità.
 * @param abilityDescription La descrizione dell'abilità.
 * @param typeColor Il primo colore associato al primo tipo del Pokemon di cui si sta mostrando l'abilità.
 * @param backgroundColor Il secondo colore associato al primo tipo del Pokemon di cui si sta mostrando l'abilità.
 */
@Composable
fun AbilityItem(
    abilityName: String,
    abilityDescription: String,
    typeColor: Color,
    backgroundColor: Color
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(typeColor, RoundedCornerShape(10.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = abilityName,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f)
            )
            Arrow(isExpanded = isExpanded)
        }

        if (isExpanded) {
            Text(
                text = abilityDescription,
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(backgroundColor, RoundedCornerShape(10.dp))
                    .padding(16.dp)
            )
        }
    }
}
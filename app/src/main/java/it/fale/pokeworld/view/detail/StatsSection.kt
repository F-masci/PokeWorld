package it.fale.pokeworld.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.ui.theme.WhiteDetails
import it.fale.pokeworld.ui.theme.detail.StatsSectionConstants

/**
 * Composable per la sezione relativa alle statistiche del Pokemon.
 *
 * @param pokemon Il Pokemon di cui mostrare le statistiche.
 */
@Composable
fun StatsSection(pokemon: PokemonEntity){

    Column(
        modifier = Modifier
            .background(WhiteDetails)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(R.string.stats),
            fontSize = 14.sp,
            modifier = Modifier.padding(StatsSectionConstants.sectionPadding)
        )
        Column {
            Row {
                Text(
                    text = stringResource(R.string.hp) + ": ${pokemon.hp}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.hp),
                            RoundedCornerShape(StatsSectionConstants.statLabelRoundedCornerShape)
                        )
                        .width(StatsSectionConstants.statLabelWidth)
                        .padding(StatsSectionConstants.statLabelPadding),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(StatsSectionConstants.rowSpacerWidth))
                Text(
                    text = stringResource(R.string.attack) + ": ${pokemon.attack}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.attack),
                            RoundedCornerShape(StatsSectionConstants.statLabelRoundedCornerShape)
                        )
                        .width(StatsSectionConstants.statLabelWidth)
                        .padding(StatsSectionConstants.statLabelPadding),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(StatsSectionConstants.columnSpacerHeight))
            Row {
                Text(
                    text = stringResource(R.string.defense) + ": ${pokemon.defense}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.defense),
                            RoundedCornerShape(StatsSectionConstants.statLabelRoundedCornerShape)
                        )
                        .width(StatsSectionConstants.statLabelWidth)
                        .padding(StatsSectionConstants.statLabelPadding),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(StatsSectionConstants.rowSpacerWidth))
                Text(
                    text = stringResource(R.string.speed) + ": ${pokemon.speed}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.speed),
                            RoundedCornerShape(StatsSectionConstants.statLabelRoundedCornerShape)
                        )
                        .width(StatsSectionConstants.statLabelWidth)
                        .padding(StatsSectionConstants.statLabelPadding),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(StatsSectionConstants.columnSpacerHeight))
            Row {
                Text(
                    text = stringResource(R.string.special_defense) + ": ${pokemon.specialDefense}",modifier = Modifier
                        .background(
                            colorResource(id = R.color.special_defense),
                            RoundedCornerShape(StatsSectionConstants.statLabelRoundedCornerShape)
                        )
                        .width(StatsSectionConstants.statLabelWidth)
                        .padding(StatsSectionConstants.statLabelPadding),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(StatsSectionConstants.rowSpacerWidth))
                Text(
                    text = stringResource(R.string.special_attack) + ": ${pokemon.specialAttack}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.special_attack),
                            RoundedCornerShape(StatsSectionConstants.statLabelRoundedCornerShape)
                        )
                        .width(StatsSectionConstants.statLabelWidth)
                        .padding(StatsSectionConstants.statLabelPadding),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(StatsSectionConstants.columnSpacerHeight))
        }
    }
}
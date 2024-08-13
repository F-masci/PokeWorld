package it.fale.pokeworld.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.WhiteDetails
import it.fale.pokeworld.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailsScreen (
    pokemonDetailViewModel: PokemonDetailViewModel
)
{

    val pokemon = pokemonDetailViewModel.pokemon.collectAsStateWithLifecycle().value
    val isDetailsLoaded = pokemonDetailViewModel.detailsLoaded.collectAsStateWithLifecycle().value

    if(isDetailsLoaded)
        DetailsCard(pokemon)
    else
        Loader()

}

@Composable
fun Loader() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Loading...")
    }
}

@Composable
fun DetailsCard(pokemon: PokemonEntity){
    pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = it
        ) {
            val spriteUrl = pokemon.spriteDefault
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = pokemon.name, modifier = Modifier
                            .background(colorResource(pokemon.type1.backgroundTextColor))
                            .fillMaxWidth()
                            .padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TypeRow(type = pokemon.type1)
                        if (pokemon.type2 != null) {
                            Spacer(modifier = Modifier.width(20.dp))
                            TypeRow(type = pokemon.type2)
                        }
                    }
                    AsyncImage(
                        model = spriteUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(5))
                            .width(350.dp)
                            .height(300.dp)
                    )
                    Row(
                        modifier = Modifier.padding(0.dp, 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Height: ${pokemon.height}",
                            modifier = Modifier
                                .background(
                                    colorResource(id = pokemon.type1.backgroundTextColor),
                                    RoundedCornerShape(15)
                                )
                                .width(170.dp)
                                .padding(15.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "Weight: ${pokemon.weight}",
                            modifier = Modifier
                                .background(
                                    colorResource(id = pokemon.type1.backgroundTextColor),
                                    RoundedCornerShape(15)
                                )
                                .width(170.dp)
                                .padding(15.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(10.dp))
                    StatsSection(pokemon = pokemon)
                }
                item {
                    Spacer(Modifier.height(10.dp))
                    AbilitiesSection(pokemon = pokemon)
                }
                item {
                    Spacer(Modifier.height(10.dp))
                    ItemSelection(pokemon = pokemon)
                }
                item{
                    Spacer(Modifier.height(10.dp))
                    MovesSelection(pokemon=pokemon)
                }

            }
        }
    }
}

@Composable
fun MovesSelection(pokemon: PokemonEntity) {
    pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "MOVES",
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
                type = pokemon.type1

            )
        }
    }
    }
}

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
                colorResource(type.backgroundTextColor)
                    .copy(alpha = 0.8f), RoundedCornerShape(10.dp)
            )
            .padding(15.dp)
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
                text = "Priority: ${priority ?: "-"}",
                fontSize = 8.sp,
                modifier = Modifier
                    .background(Color.Red, RoundedCornerShape(10.dp))
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Descrizione",
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
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            ValueMoves("Accuracy", accuracy)
            ValueMoves("Effect Chance", effectChance)
            ValueMoves("Power", power)
        }

    }
}

@Composable
fun ItemSelection(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "ITEMS",
            fontSize = 1.sp,
            modifier = Modifier.padding(15.dp)
        )
        pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
             pokemon.items.forEach { item ->
                ItemCard(
                    itemName = item.getLocaleName(),
                    itemCost = item.cost,
                    itemEffect = item.getLocaleEffect(),
                    itemSprite = item.sprite,
                    typeColor = colorResource(pokemon.type1.backgroundTextColor),
                )
            }

        }
        }

    }
}

@Composable
fun ItemCard(
    itemName: String,
    itemCost: Int,
    itemEffect: String,
    itemSprite: String?,
    typeColor: Color

) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 2.dp,
                color = typeColor,
                shape = RoundedCornerShape(10.dp)
            )
            .background(WhiteDetails, RoundedCornerShape(10.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
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
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = itemName,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Arrow(isExpanded)
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("Cost:")
                    }
                    append(" $itemCost")
                },
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append("Effect:")
                    }
                    append(" $itemEffect")
                },
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun AbilitiesSection(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ABILITIES",
            fontSize = 14.sp,
            modifier = Modifier.padding(15.dp)
        )
        pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
            pokemon.abilities.forEach { ability ->
                AbilityItem(
                    abilityName = ability.getLocaleName(),
                    abilityDescription = ability.getLocaleEffect(),
                    typeColor = colorResource(pokemon.type1.backgroundTextColor),
                    backgroundColor=it
                )
            }
        }
    }
}


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
                    .padding(top = 8.dp)
                    .background(backgroundColor, RoundedCornerShape(10.dp))
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun StatsSection(pokemon: PokemonEntity){

    Column(
        modifier = Modifier
            .background(WhiteDetails)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
       Text(text = "STATS", fontSize = 14.sp, modifier = Modifier.padding(15.dp))
        Column {
            Row {
                Text(text = "HP: ${pokemon.hp}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.hp),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Attack: ${pokemon.attack}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.attack),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Defense: ${pokemon.defense}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.defense),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Speed: ${pokemon.speed}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.speed),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(text = "Special Defense: ${pokemon.specialDefense}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.special_defense),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Special attack: ${pokemon.specialAttack}",
                    modifier = Modifier
                        .background(
                            colorResource(id = R.color.special_attack),
                            RoundedCornerShape(15)
                        )
                        .width(170.dp)
                        .padding(15.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
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
@Composable
fun Arrow(isExpanded: Boolean){
    Icon(
        painter = painterResource(id = if (isExpanded) R.drawable.dropup_arrow else R.drawable.dropdown_arrow),
        contentDescription = null,
        modifier = Modifier.size(24.dp))
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
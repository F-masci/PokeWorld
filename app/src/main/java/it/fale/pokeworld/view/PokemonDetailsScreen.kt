package it.fale.pokeworld.view

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
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
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import androidx.compose.material3.AlertDialog
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.WhiteDetails
import it.fale.pokeworld.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailsScreen (
    pokemonDetailViewModel: PokemonDetailViewModel,
    pokemonId: Int
)
{

    val pokemon: PokemonEntity? = pokemonDetailViewModel.pokemon.collectAsStateWithLifecycle().value
    val isDetailsLoaded = pokemonDetailViewModel.detailsLoaded.collectAsStateWithLifecycle().value

    if(!isDetailsLoaded || pokemon?.id != pokemonId) pokemonDetailViewModel.loadPokemon(pokemonId)

    if(isDetailsLoaded)
        DetailsCard(pokemonDetailViewModel, pokemon!!)
    else
        Loader()

}



@Composable
fun Loader() {
    val context = LocalContext.current
    val gradient = Brush.radialGradient(
        0.0f to colorResource(id = R.color.gradient_light),
        0.8f to colorResource(id = R.color.gradient_dark),
        radius = 1800.0f,
        tileMode = TileMode.Repeated)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.pokeball)
                .decoderFactory(ImageDecoderDecoder.Factory())
                .build(),
            contentDescription = "Animated loading GIF",
            modifier = Modifier
                .width(180.dp)
                .height(180.dp)
        )
    }
}

@Composable
fun DetailsCard(pokemonDetailViewModel: PokemonDetailViewModel, pokemon: PokemonEntity){
    val context = LocalContext.current
    var currentFavoriteId = pokemonDetailViewModel.getFavoritePokemonId(context)
    var isFavorite by remember { mutableStateOf(currentFavoriteId == pokemon.id) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = it
        ) {
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
                    Box(modifier = Modifier
                        .width(350.dp)
                        .height(300.dp),
                        contentAlignment = Alignment.Center) {

                        Canvas(modifier = Modifier
                            .height(350.dp)
                            .width(350.dp)
                            .background(Color.White.copy(0.8f), RoundedCornerShape(10))) {}

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(pokemon.getAnimatedImageUrl())
                                .decoderFactory(ImageDecoderDecoder.Factory())
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .width(350.dp)
                                .height(200.dp)
                        )

                        Row( modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween )
                        {
                            IconButton(
                                onClick = {
                                    playAudio(pokemon)
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.audio),
                                    contentDescription = "Play audio",
                                    tint = Color.Unspecified
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (isFavorite) {//entro qui solo se cerco di togliere il pokemon che è già tra i preferiti
                                        isFavorite = false
                                        pokemonDetailViewModel.clearFavoritePokemon(context)
                                    } else if (currentFavoriteId != null && currentFavoriteId != pokemon.id) {//solo se esiste già un preferito ma non è il pokemon della schermata attuale
                                        showConfirmationDialog = true
                                    } else {
                                        isFavorite = true
                                        pokemonDetailViewModel.saveFavoritePokemon(context)
                                    }
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = if (isFavorite) R.drawable.yellowstar else R.drawable.star),
                                    contentDescription = "Add to favourites",
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                    if (showConfirmationDialog) {
                        ConfirmFavoriteChangeDialog(
                            onConfirm = {
                                isFavorite = true
                                pokemonDetailViewModel.saveFavoritePokemon(context)
                                currentFavoriteId=pokemon.id
                                showConfirmationDialog = false
                            },
                            onCancel = {
                                showConfirmationDialog = false
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.padding(0.dp, 15.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.height) + ": ${pokemon.height}",
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
                            text = stringResource(R.string.weight) + ": ${pokemon.weight}",
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
                    Text(
                        text = stringResource(R.string.moves),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(15.dp)
                    )
                }
                items(pokemon.moves) { move ->
                    MoveItem(
                        moveName = move.getLocaleName(LocalContext.current),
                        moveDescription = move.getLocaleDescription(LocalContext.current),
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
}

@Composable
fun ConfirmFavoriteChangeDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = stringResource(R.string.change_favorite))
        },
        text = {
            Text(stringResource(R.string.change_favorite_disclaimer))
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
            ) {
                Text(stringResource(R.string.no))
            }
        }
    )
}


fun playAudio(pokemon: PokemonEntity) {

    val mediaPlayer: MediaPlayer?

    mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource(pokemon.criesLatest)
    mediaPlayer.prepare()
    mediaPlayer.start()
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
fun ItemSelection(pokemon: PokemonEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(R.string.items),
            fontSize = 14.sp,
            modifier = Modifier.padding(15.dp)
        )
        pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
             pokemon.items.forEach { item ->
                ItemCard(
                    itemName = item.getLocaleName(LocalContext.current),
                    itemCost = item.cost,
                    itemEffect = item.getLocaleEffect(LocalContext.current),
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
                        append(stringResource(R.string.cost) + ":")
                    }
                    append(" $itemCost")
                },
                fontSize = 11.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
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
                    .padding(top = 4.dp)
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
            text = stringResource(R.string.abilities),
            fontSize = 14.sp,
            modifier = Modifier.padding(15.dp)
        )
        pokemon.type1?.let { colorResource(id = it.backgroundColor) }?.let {
            pokemon.abilities.forEach { ability ->
                AbilityItem(
                    abilityName = ability.getLocaleName(LocalContext.current),
                    abilityDescription = ability.getLocaleEffect(LocalContext.current),
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
                    .fillMaxWidth()
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
       Text(text = stringResource(R.string.stats), fontSize = 14.sp, modifier = Modifier.padding(15.dp))
        Column {
            Row {
                Text(text = stringResource(R.string.hp) + ": ${pokemon.hp}",
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
                Text(text = stringResource(R.string.attack) + ": ${pokemon.attack}",
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
                Text(text = stringResource(R.string.defense) + ": ${pokemon.defense}",
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
                Text(text = stringResource(R.string.speed) + ": ${pokemon.speed}",
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
                Text(text = stringResource(R.string.special_defense) + ": ${pokemon.specialDefense}",
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
                Text(text = stringResource(R.string.special_attack) + ": ${pokemon.specialAttack}",
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
        .width(150.dp)
        .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Image(
            painterResource(id = type.icon), "type_icon",
            modifier = Modifier
                .padding(2.dp)
                .height(20.dp))
        Text(stringResource(id = type.string), fontSize = 10.sp)
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
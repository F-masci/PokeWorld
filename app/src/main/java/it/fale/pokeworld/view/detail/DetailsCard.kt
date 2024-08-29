package it.fale.pokeworld.view.detail

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import it.fale.pokeworld.R
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonType
import it.fale.pokeworld.ui.theme.detail.DetailsCardConstants
import it.fale.pokeworld.ui.theme.detail.MovesSectionConstants
import it.fale.pokeworld.utils.isNetworkAvailable
import it.fale.pokeworld.viewmodel.PokemonDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

/**
 * Composable per la pagina di dettaglio.
 *
 * @param pokemonDetailViewModel Il ViewModel per il dettaglio dei Pokemon.
 * @param pokemon Il Pokemon di cui mostrare i dettagli.
 */
@Composable
fun DetailsCard(pokemonDetailViewModel: PokemonDetailViewModel, pokemon: PokemonEntity){
    val context = LocalContext.current
    var currentFavoriteId = pokemonDetailViewModel.getFavoritePokemonId()
    var isFavorite by remember { mutableStateOf(currentFavoriteId == pokemon.id) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val backgroundColor = pokemon.type1?.getBackgroundColor?.invoke() ?: Color.White
    val backgroundTextColor = pokemon.type1?.getBackgroundTextColor?.invoke() ?: Color.White

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(DetailsCardConstants.lazyColumnPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = pokemon.name, modifier = Modifier
                        .background(backgroundTextColor)
                        .fillMaxWidth()
                        .padding(DetailsCardConstants.generalPaddingSize),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DetailsCardConstants.generalPaddingSize),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TypeRow(type = pokemon.type1!!)
                    if (pokemon.type2 != null) {
                        Spacer(modifier = Modifier.width(DetailsCardConstants.typeRowSpacerWidth))
                        TypeRow(type = pokemon.type2)
                    }
                }
                Box(modifier = Modifier
                    .width(DetailsCardConstants.generalWidth)
                    .height(DetailsCardConstants.boxHeight),
                    contentAlignment = Alignment.Center) {

                    Canvas(modifier = Modifier
                        .height(DetailsCardConstants.canvasHeight)
                        .width(DetailsCardConstants.generalWidth)
                        .background(Color.White.copy(DetailsCardConstants.backgroundOpacity), RoundedCornerShape(DetailsCardConstants.roundedCornerPercentage))) {}

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.getAnimatedImageUrl())
                            .decoderFactory(ImageDecoderDecoder.Factory())
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .width(DetailsCardConstants.generalWidth)
                            .height(DetailsCardConstants.imageHeight)
                    )

                    Row( modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(DetailsCardConstants.iconPadding),
                        horizontalArrangement = Arrangement.SpaceBetween )
                    {
                        IconButton(
                            onClick = {
                                playAudio(context, pokemon)
                            },
                            modifier = Modifier
                                .padding(DetailsCardConstants.iconPadding)
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
                                    pokemonDetailViewModel.clearFavoritePokemon()
                                } else if (currentFavoriteId != null && currentFavoriteId != pokemon.id) {//solo se esiste già un preferito ma non è il pokemon della schermata attuale
                                    showConfirmationDialog = true
                                } else {
                                    isFavorite = true
                                    pokemonDetailViewModel.saveFavoritePokemon()
                                }
                            },
                            modifier = Modifier
                                .padding(DetailsCardConstants.iconPadding)
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
                            pokemonDetailViewModel.saveFavoritePokemon()
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
                                backgroundTextColor,
                                RoundedCornerShape(DetailsCardConstants.textRoundedCornerPercentage)
                            )
                            .width(DetailsCardConstants.textWidth)
                            .padding(DetailsCardConstants.textPadding),
                        textAlign = TextAlign.Center,
                        fontSize = DetailsCardConstants.fontSize
                    )
                    Spacer(modifier = Modifier.width(DetailsCardConstants.spacerWidth))
                    Text(
                        text = stringResource(R.string.weight) + ": ${pokemon.weight}",
                        modifier = Modifier
                            .background(
                                backgroundTextColor,
                                RoundedCornerShape(DetailsCardConstants.textRoundedCornerPercentage)
                            )
                            .width(DetailsCardConstants.textWidth)
                            .padding(DetailsCardConstants.textPadding),
                        textAlign = TextAlign.Center,
                        fontSize = DetailsCardConstants.fontSize
                    )
                }
            }
            item {
                Spacer(Modifier.height(DetailsCardConstants.itemSpacerHeight))
                StatsSection(pokemon = pokemon)
            }
            item {
                Spacer(Modifier.height(DetailsCardConstants.itemSpacerHeight))
                AbilitiesSection(pokemon = pokemon)
            }
            item {
                Spacer(Modifier.height(DetailsCardConstants.itemSpacerHeight))
                ItemSection(pokemon = pokemon)
            }

            // Moves section
            item {
                Spacer(Modifier.height(DetailsCardConstants.itemSpacerHeight))
                Text(
                    text = stringResource(R.string.moves),
                    fontSize = MovesSectionConstants.titleFontSize,
                    modifier = Modifier.padding(MovesSectionConstants.sectionPadding)
                )
            }
            items (pokemon.moves) { move ->
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
    }
}

/**
 *  Composable per l'etichetta per mostrare i tipi del Pokemon.
 *
 *  @param type Il tipo da mostrare
 */
@Composable
fun TypeRow(type: PokemonType) {

    Row (modifier = Modifier
        .background(
            type.getBackgroundTextColor().copy(alpha = DetailsCardConstants.backgroundOpacity),
            RoundedCornerShape(DetailsCardConstants.typeRowRoundedCornerPercentage)
        )
        .width(DetailsCardConstants.typeRowWidth)
        .padding(DetailsCardConstants.typeRowPadding),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically){
        Image(
            painterResource(id = type.icon), "type_icon",
            modifier = Modifier
                .padding(DetailsCardConstants.typeRowImagePadding)
                .height(DetailsCardConstants.typeRowImageHeight))
        Text(stringResource(id = type.string), fontSize = DetailsCardConstants.typeRowFontSize)
    }
}

/**
 * Composable per la finestra di dialogo per cambiare il Pokemon preferito.
 *
 * @param onConfirm Callback da eseguire a seguito di conferma da parte dell'utente.
 * @param onCancel Callback da eseguire a seguito di annullamento.
 */
@Composable
fun ConfirmFavoriteChangeDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {

    val titleText = stringResource(R.string.change_favorite)
    val messageText = stringResource(R.string.change_favorite_disclaimer)
    val confirmButtonText = stringResource(R.string.yes)
    val cancelButtonText = stringResource(R.string.no)

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = titleText)
        },
        text = {
            Text(text = messageText)
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
            ) {
                Text(
                    text = confirmButtonText,
                    textAlign = TextAlign.Center,
                    fontSize = DetailsCardConstants.fontSize
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
            ) {
                Text(text = cancelButtonText)
            }
        }
    )
}

/**
 * Funzione per riprodurre il verso del Pokemon.
 *
 * @param context Il contesto in cui viene eseguita la funzione.
 * @param pokemon Il Pokemon di cui riprodurre il suono
 */
fun playAudio(context: Context, pokemon: PokemonEntity) {

    if (!isNetworkAvailable(context)) {
        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show()
        return
    }

    val mediaPlayer = MediaPlayer()

    CoroutineScope(Dispatchers.IO).launch {
        try {
            withTimeout(5000L) {
                mediaPlayer.setDataSource(pokemon.criesLatest)
                mediaPlayer.prepare()
                withContext(Dispatchers.Main) {
                    mediaPlayer.start()
                }
            }
        } catch (e: TimeoutCancellationException) {
            withContext(Dispatchers.Main) {
                mediaPlayer.release()
                Toast.makeText(context, R.string.unstable_internet, Toast.LENGTH_SHORT).show()
            }
        } finally {
            withContext(Dispatchers.Main) {
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                }
            }
        }
    }
}

/**
 * Composable per mostrare la freccia.
 *
 * @param isExpanded Determina se la freccia sarà diretta verso l'alto o verso il basso.
 */
@Composable
fun Arrow(isExpanded: Boolean){
    Icon(
        painter = painterResource(id = if (isExpanded) R.drawable.dropup_arrow else R.drawable.dropdown_arrow),
        contentDescription = null,
        modifier = Modifier.size(24.dp))
}
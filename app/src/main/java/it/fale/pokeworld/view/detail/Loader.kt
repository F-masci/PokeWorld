package it.fale.pokeworld.view.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import it.fale.pokeworld.R
import it.fale.pokeworld.ui.theme.detail.LoaderConstants

/**
 * Composable per la schermata di caricamento della pagina di dettaglio.
 */
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
            .padding(LoaderConstants.columnPadding),
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
                .width(LoaderConstants.imageWidth)
                .height(LoaderConstants.imageHeight)
        )
    }
}

package it.fale.pokeworld.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import it.fale.pokeworld.R
import kotlinx.coroutines.delay


@Composable
fun WelcomeScreen(
    navController: NavController,
) {
    LaunchedEffect(Unit) {  //API di Jetpack Compose che viene utilizzata per avviare effetti collaterali durante la composizione e viene visualizzata soltanto all'avvio
        delay(2000L) // Aspetta 2 secondi
        navController.navigate("pokemon_list_screen") {
            // Rimuove questa schermata dallo stack in modo che l'utente non possa tornare indietro
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true //indico che ho intenzione di rimuovere la schermata dallo stack
            }
        }
    }
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
                .data(R.drawable.pikachu) //ho inserito questa gif solo per test, ne possiamo anche scegliere un altra
                .decoderFactory(ImageDecoderDecoder.Factory())
                .build(),
            contentDescription = "Animated GIF",
        )
        Spacer(modifier = Modifier.height(5.dp))
        Image(painterResource(id = R.drawable.logo2), "logo", modifier = Modifier.size(180.dp).fillMaxHeight())
        //Text("Loading...", color = Color.White, fontFamily = pokemonPixelFont, fontSize = 14.sp)

    }
}
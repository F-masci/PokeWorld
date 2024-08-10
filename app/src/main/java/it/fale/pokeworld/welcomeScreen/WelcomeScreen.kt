package it.fale.pokeworld.welcomeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.pikachu) //ho inserito questa gif solo per test, ne possiamo anche scegliere un altra
                .decoderFactory(ImageDecoderDecoder.Factory())
                .build(),
            contentDescription = "Animated GIF"
        )//
    }
}
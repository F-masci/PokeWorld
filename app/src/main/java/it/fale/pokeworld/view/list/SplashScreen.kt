package it.fale.pokeworld.view.list

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.fale.pokeworld.R

/**
 * Composable per la splash screen dell'applicazione.
 */
@Composable
fun SplashScreen() {

    // Colore di sfondo
    val gradient = Brush.radialGradient(
        0.0f to colorResource(id = R.color.gradient_light),
        0.8f to colorResource(id = R.color.gradient_dark),
        radius = 1800.0f,
        tileMode = TileMode.Repeated
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Immagina statica di Pikachu
        Image(
            painter = painterResource(R.drawable.pikachu_static),
            contentDescription = "Pikachu",
            modifier = Modifier
                .size(200.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        // Logo dell'applicazione
        Image(
            painterResource(id = R.drawable.logo2),
            contentDescription = "Logo PokeWorld",
            modifier = Modifier
                .size(180.dp)
                .fillMaxHeight())

    }
}
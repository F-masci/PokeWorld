package it.fale.pokeworld.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.R

// declare the custom default font family

val pokemonPixelFont = FontFamily (
    Font(R.font.pokemon_pixel, FontWeight.Light)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = pokemonPixelFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val LocalThemedTypography = staticCompositionLocalOf { ThemedTypography() }

@Immutable
data class ThemedTypography(

    val drawerItemLabel: TextStyle = TextStyle.Default,
    val drawerOptionsLabel: TextStyle = TextStyle.Default,
    val drawerDropdownItemLabel: TextStyle = TextStyle.Default,

    val topbarFilterTypeOptionsLabel: TextStyle = TextStyle.Default,
    val topbarFilterTypeLabel: TextStyle = TextStyle.Default,

    val pokemonCardTitle: TextStyle = TextStyle.Default
)

val mainThemedTypography = ThemedTypography(

    drawerItemLabel = TextStyle(
        fontFamily = pokemonPixelFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    drawerOptionsLabel = TextStyle(
        fontFamily = pokemonPixelFont,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp
    ),
    drawerDropdownItemLabel = TextStyle(
        fontFamily = pokemonPixelFont,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),

    topbarFilterTypeOptionsLabel = TextStyle(
        fontSize = 12.sp,
        fontFamily = pokemonPixelFont
    ),
    topbarFilterTypeLabel = TextStyle(
        fontSize = 10.sp,
        fontFamily = pokemonPixelFont
    ),

    pokemonCardTitle = TextStyle(
        fontFamily = pokemonPixelFont,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    )

)

val MaterialTheme.themedTypography: ThemedTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalThemedTypography.current
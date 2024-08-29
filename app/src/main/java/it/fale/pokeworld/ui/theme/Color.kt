package it.fale.pokeworld.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val WhiteDetails = Color.White.copy(alpha = 0.6f)

val DarkColorScheme = darkColorScheme()
val LightColorScheme = lightColorScheme()

val LocalThemedColorsPalette = staticCompositionLocalOf { ThemedColorsPalette() }

@Immutable
data class ThemedColorsPalette(

    /* --- Main --- */

    val mainListBackgroundColor: Color = Color.Unspecified,
    val mainTextColor: Color = Color.Unspecified,

    val mainBlue: Color = Color.Unspecified,
    val mainLightBlue: Color = Color.Unspecified,
    val mainYellow: Color = Color.Unspecified,
    val mainLightYellow: Color = Color.Unspecified,

    /* --- Reverse --- */

    val reverseTextColor: Color = Color.Unspecified,

    /* --- Drawer --- */

    val drawerOptionsBackgroundColor: Color = Color.Unspecified,

    /* --- Topbar --- */

    val topbarFilterTypeTextColor: Color = Color.Unspecified,

    /* --- List --- */

    val mainListCardBorder: Color = Color.Unspecified,

    /* --- Pokemon --- */

    val pokemonImageBackground: Color = Color.Unspecified,

    /* --- Tipi --- */

    val normalBackground: Color = Color.Unspecified,
    val normalTextBackground: Color = Color.Unspecified,
    val grassBackground: Color = Color.Unspecified,
    val grassTextBackground: Color = Color.Unspecified,
    val fightingBackground: Color =Color.Unspecified,
    val fightingTextBackground: Color = Color.Unspecified,
    val flyingBackground: Color = Color.Unspecified,
    val flyingTextBackground: Color = Color.Unspecified,
    val poisonBackground: Color = Color.Unspecified,
    val poisonTextBackground: Color = Color.Unspecified,
    val groundBackground: Color = Color.Unspecified,
    val groundTextBackground: Color = Color.Unspecified,
    val rockBackground: Color = Color.Unspecified,
    val rockTextBackground: Color = Color.Unspecified,
    val bugBackground: Color = Color.Unspecified,
    val bugTextBackground: Color = Color.Unspecified,
    val ghostBackground: Color = Color.Unspecified,
    val ghostTextBackground: Color = Color.Unspecified,
    val steelBackground: Color = Color.Unspecified,
    val steelTextBackground: Color = Color.Unspecified,
    val fireBackground: Color = Color.Unspecified,
    val fireTextBackground: Color = Color.Unspecified,
    val waterBackground: Color = Color.Unspecified,
    val waterTextBackground: Color = Color.Unspecified,
    val electricBackground: Color = Color.Unspecified,
    val electricTextBackground: Color = Color.Unspecified,
    val psychicBackground: Color = Color.Unspecified,
    val psychicTextBackground: Color = Color.Unspecified,
    val iceBackground: Color = Color.Unspecified,
    val iceTextBackground: Color = Color.Unspecified,
    val dragonBackground: Color = Color.Unspecified,
    val dragonTextBackground: Color = Color.Unspecified,
    val darkBackground: Color = Color.Unspecified,
    val darkTextBackground: Color = Color.Unspecified,
    val fairyBackground: Color = Color.Unspecified,
    val fairyTextBackground: Color = Color.Unspecified,
    val stellarBackground: Color = Color.Unspecified,
    val stellarTextBackground: Color = Color.Unspecified,
    val shadowBackground: Color = Color.Unspecified,
    val shadowTextBackground: Color = Color.Unspecified
)

val lightThemedColorsPalette = ThemedColorsPalette(

    /* --- Main --- */

    mainListBackgroundColor = Color.White,
    mainTextColor = Color.Black,

    mainBlue = Color(0xFF356ABC),
    mainLightBlue = Color(0xFF5F83BB),
    mainYellow = Color(0xFFFACD01),
    mainLightYellow = Color(0xFFFFE058),

    /* --- Reverse --- */

    reverseTextColor = Color.White,

    /* --- Drawer --- */

    drawerOptionsBackgroundColor = Color.DarkGray,

    /* --- Topbar --- */

    topbarFilterTypeTextColor = Color.Black,

    /* --- List --- */

    mainListCardBorder = Color.DarkGray,

    /* --- Pokemon --- */

    pokemonImageBackground = Color.White.copy(0.6f),

    /* --- Tipi --- */

    normalBackground = Color(0xFFCCCCCC),
    normalTextBackground = Color(0xFF888888),
    grassBackground = Color(0xFFADE6A8),
    grassTextBackground = Color(0xFF7BD573),
    fightingBackground = Color(0xFFFFCC99),
    fightingTextBackground = Color(0xFFFF9933),
    flyingBackground = Color(0xFFCCE6FF),
    flyingTextBackground = Color(0xFF66B5FF),
    poisonBackground = Color(0xFFD9B3FF),
    poisonTextBackground = Color(0xFFB366FF),
    groundBackground = Color(0xFFEEA987),
    groundTextBackground = Color(0xFFB34D19),
    rockBackground = Color(0xFFD6D6C2),
    rockTextBackground = Color(0xFF8A8A5C),
    bugBackground = Color(0xFFBDC997),
    bugTextBackground = Color(0xFF95A857),
    ghostBackground = Color(0xFFDF9FBF),
    ghostTextBackground = Color(0xFFBF4080),
    steelBackground = Color(0xFFB3CCFF),
    steelTextBackground = Color(0xFF6699FF),
    fireBackground = Color(0xFFFF9980),
    fireTextBackground = Color(0xFFFF471A),
    waterBackground = Color(0xFF99C2FF),
    waterTextBackground = Color(0xFF3385FF),
    electricBackground = Color(0xFFFFEB99),
    electricTextBackground = Color(0xFFFFD633),
    psychicBackground = Color(0xFFFF80BF),
    psychicTextBackground = Color(0xFFFF3399),
    iceBackground = Color(0xFFADEBEB),
    iceTextBackground = Color(0xFF5BD7D7),
    dragonBackground = Color(0xFFADADEB),
    dragonTextBackground = Color(0xFF5B5BD7),
    darkBackground = Color(0xFFBDB3A8),
    darkTextBackground = Color(0xFF746658),
    fairyBackground= Color(0xFFFF99FF),
    fairyTextBackground = Color(0xFFFF33FF),
    stellarBackground = Color(0xFF99FFE6),
    stellarTextBackground = Color(0xFF1AFFC6),
    shadowBackground = Color(0xFFB3B3E6),
    shadowTextBackground = Color(0xFF6666CC)
)

// Inserire solo i colori differenti dalla palette chiara
val darkThemedColorsPalette = lightThemedColorsPalette.copy(

    /* --- Main --- */

    mainListBackgroundColor = Color(0xFF192734),
    mainTextColor = Color.White,

    /* --- Reverse --- */

    reverseTextColor = Color.Black,

    /* --- Drawer --- */

    drawerOptionsBackgroundColor = Color.LightGray

)

val MaterialTheme.themedColorsPalette: ThemedColorsPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalThemedColorsPalette.current
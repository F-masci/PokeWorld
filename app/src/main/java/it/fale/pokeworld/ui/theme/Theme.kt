package it.fale.pokeworld.ui.theme

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import java.util.Locale

/**
 * Composable per il tema e la lingua della UI.
 *
 * @param darkTheme Indica se il tema è scuro.
 * @param languageCode Il codice della lingua da utilizzare.
 * @param context Il contesto corrente dell'applicazione.
 * @param dynamicColor Indica se utilizzare i colori dinamici.
 * @param content Il contenuto della UI.
 */
@Composable
fun PokeWorldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    languageCode: String = Locale.getDefault().language,
    context: Context = LocalContext.current,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    // Ottiene il contesto con la lingua specificata
    val localizedContext = context.getLocalizedContext(languageCode)

    // Ottiene i colori personalizzati e il tipografia da utilizzare
    val themedColorsPalette = if(darkTheme) darkThemedColorsPalette else lightThemedColorsPalette
    val themedTypography = mainThemedTypography

    // Imposta lo schema di colori di default per il tema
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(localizedContext) else dynamicLightColorScheme(localizedContext)

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    // Fornisce lo schema di colori personale e la tipografia ai componenti dell'applicazione
    // Il contesto corrente dell'applicazione viene sovrascritto con quello modificato
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalThemedColorsPalette provides themedColorsPalette,
        LocalThemedTypography provides themedTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

/**
 * Ottiene un contesto con la lingua specificata.
 *
 * @param languageCode Il codice della lingua da utilizzare.
 * @return Il contesto con la lingua specificata.
 */
fun Context.getLocalizedContext(languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    config.setLayoutDirection(locale)

    return createConfigurationContext(config)
}
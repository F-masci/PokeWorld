package it.fale.pokeworld.view.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import it.fale.pokeworld.R
import it.fale.pokeworld.ui.theme.list.DrawerConstants
import it.fale.pokeworld.ui.theme.themedColorsPalette
import it.fale.pokeworld.ui.theme.themedTypography
import it.fale.pokeworld.utils.Language

/**
 * Composable per il drawer di impostazioni.
 *
 * @param drawerState Lo stato del drawer. Consente di aprire e chiudere il drawer.
 * @param isDarkTheme Indica se il tema corrente è scuro.
 * @param language La lingua corrente.
 * @param onThemeToggle Callback per notificare il cambio di tema.
 * @param onLanguageChange Callback per notificare il cambio di lingua.
 * @param content Il contenuto sotto al drawer.
 */
@Composable
fun SettingsDrawer(
    drawerState: DrawerState,
    isDarkTheme: Boolean,
    language: String,
    onThemeToggle: (Boolean) -> Unit,
    onLanguageChange: (selectedLanguage: Language) -> Unit,
    content: @Composable () -> Unit
) {

    // Contenuto del modal
    ModalDrawer (
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                isDarkTheme = isDarkTheme,
                language = language,
                onThemeToggle = onThemeToggle,
                onLanguageChange = onLanguageChange
            )
        },
        // Contenuto sotto il modal
        content = content
    )

}

/**
 * Composable per il contenuto del drawer.
 *
 * @param isDarkTheme Indica se il tema corrente è scuro.
 * @param language La lingua corrente.
 * @param onThemeToggle Callback per notificare il cambio di tema.
 * @param onLanguageChange Callback per notificare il cambio di lingua.
 */
@Composable
fun DrawerContent(
    isDarkTheme: Boolean,
    language: String,
    onThemeToggle: (Boolean) -> Unit,
    onLanguageChange: (selectedLanguage: Language) -> Unit
) {

    // Contiene lo stato del dropDown menu
    var expandedState by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.themedColorsPalette.mainListBackgroundColor)
    ) {

        // Switch per cambiare il tema
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(DrawerConstants.labelPaddingSize)
        ) {
            // Label per l'opzione del tema
            Text(
                stringResource(R.string.theme),
                style = MaterialTheme.themedTypography.drawerItemLabel,
                color = MaterialTheme.themedColorsPalette.mainTextColor
            )
            // Switch per cambiare il tema
            Button(
                onClick = { onThemeToggle(!isDarkTheme) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.themedColorsPalette.drawerOptionsBackgroundColor,
                ),
                modifier = Modifier
                    .background(
                        MaterialTheme.themedColorsPalette.drawerOptionsBackgroundColor,
                        RoundedCornerShape(DrawerConstants.BUTTON_CORNER_RADIUS)
                    )
            ) {
                Text(
                    text = if (isDarkTheme) stringResource(R.string.dark_theme) else stringResource(R.string.light_theme),
                    color = MaterialTheme.themedColorsPalette.reverseTextColor,
                    style = MaterialTheme.themedTypography.drawerOptionsLabel
                )
            }
        }

        // Menù a tendina per cambiare la lingua
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(DrawerConstants.labelPaddingSize)
        ) {
            // Label per l'opzione della lingua
            Text(
                stringResource(R.string.language),
                style = MaterialTheme.themedTypography.drawerItemLabel,
                color = MaterialTheme.themedColorsPalette.mainTextColor
            )
            Box {
                // Bottone per aprire il menu a tendina
                Button(
                    onClick = { expandedState = !expandedState },
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    modifier = Modifier
                        .background(
                            MaterialTheme.themedColorsPalette.drawerOptionsBackgroundColor,
                            RoundedCornerShape(DrawerConstants.BUTTON_CORNER_RADIUS)
                        )
                ) {
                    Text(
                        language,
                        style = MaterialTheme.themedTypography.drawerOptionsLabel,
                        color = MaterialTheme.themedColorsPalette.reverseTextColor,
                    )
                }
                // Menu a tendina per le lingue
                DropdownMenu(
                    expanded = expandedState,
                    onDismissRequest = { expandedState = false },
                    modifier = Modifier
                        .width(DrawerConstants.fixedDropdownWidth)
                        .height(DrawerConstants.fixedDropdownHeight)
                ) {
                    Language.entries.map { it.text }.forEach { option ->
                        DropdownMenuItem(
                            {
                                Text(
                                    option,
                                    style = MaterialTheme.themedTypography.drawerDropdownItemLabel
                                )
                            },
                            onClick = {
                                onLanguageChange(Language.fromText(option))
                                expandedState = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Alpha Version Database v1.0",
            modifier = Modifier
                .padding(DrawerConstants.labelPaddingSize),
            style = MaterialTheme.themedTypography.drawerOptionsLabel,
            color = MaterialTheme.themedColorsPalette.mainTextColor
        )
    }
}
package it.fale.pokeworld.view.list

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.DrawerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.fale.pokeworld.R
import it.fale.pokeworld.ui.theme.pokemonPixelFont
import it.fale.pokeworld.utils.Language
import kotlinx.coroutines.launch

@Composable
fun SettingsDrawer(
    drawerState: DrawerState,
    isDarkTheme: Boolean, // Ricevi il tema attuale
    language: String, // Ricevi la lingua attuale
    onThemeToggle: (Boolean) -> Unit, // Callback per notificare il cambio di tema
    onLanguageChange: (selectedLanguage: Language) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                isDarkTheme = isDarkTheme, // Passa il tema attuale
                language = language, // Passa la lingua attuale
                onThemeToggle = onThemeToggle,
                onLanguageChange = onLanguageChange,
                onItemClick = {
                    scope.launch { drawerState.close() }
                },
            )
        }
    ) {
        content()
    }
}

@Composable
fun DrawerContent(
    isDarkTheme: Boolean, // Ricevi il tema attuale
    language: String, // Ricevi la lingua attuale
    onItemClick: (String) -> Unit,
    onThemeToggle: (Boolean) -> Unit, // Callback per notificare il cambio di tema
    onLanguageChange: (selectedLanguage: Language) -> Unit
) {
    var isSwitchOn by remember { mutableStateOf(isDarkTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) colorResource(id = R.color.dark_mode_background) else Color.White)
    ) {
        // Opzioni del drawer

        // Switch per cambiare il tema
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.theme),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemClick("Home") },
                fontSize = 20.sp,
                color = if(isDarkTheme) Color.White else Color.Black
            )
            SwitchButton(isLightMode = isSwitchOn) {
                isSwitchOn = it
                onThemeToggle(it) // Notifica il cambio di tema
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                stringResource(R.string.language),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onItemClick("Home") },
                fontSize = 20.sp,
                color = if(isDarkTheme) Color.White else Color.Black
            )
            ChoiceLanguageMenu(
                initialText = language,
                expandedState = remember { mutableStateOf(false) },
                onOptionSelected = { newLanguage ->
                    onLanguageChange(Language.fromText(newLanguage))
                },
                options = Language.entries.map { it.text },
                isDarkTheme = isDarkTheme
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            "Alpha Version Database v1.0",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onItemClick("") },
            fontSize = 16.sp,
            color = if(isDarkTheme) Color.White else Color.Black
        )
    }
}




@Composable
fun SwitchButton(isLightMode: Boolean, onSwitchChange: (Boolean) -> Unit) {
    Button(
        onClick = { onSwitchChange(!isLightMode) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLightMode) Color.LightGray else Color.DarkGray
        ),
        modifier = Modifier
            .background(
                if (isLightMode) Color.LightGray else Color.DarkGray,
                RoundedCornerShape(60)
            )
        //.width(110.dp) con questa larghezza raggiungo l'uguaglianza dei due bottoni non so se mi piace
    ) {
        Text(
            text = if (isLightMode) stringResource(R.string.light_theme) else stringResource(R.string.dark_theme),
            color = if (isLightMode) Color.Black else Color.White,
            fontSize = 14.sp
        )
    }
    //}
}

@Composable
fun ChoiceLanguageMenu(
    initialText: String,
    expandedState: MutableState<Boolean>,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
    isDarkMode: Boolean
) {
    var selectedOption by remember { mutableStateOf(initialText) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var prevOption by remember { mutableStateOf("") }

    Box {
        Button(
            onClick = { expandedState.value = !expandedState.value },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            modifier = Modifier
                .background(if(isDarkMode) Color.LightGray else Color.DarkGray, RoundedCornerShape(60))
                .width(110.dp)//impostato a mano per essere uguale al button di dark-light mode
        ) {
            Text(
                selectedOption,
                fontSize = 14.sp,
                color = if(isDarkMode) Color.Black else Color.White,
                //opto di proposito per uno stile distaccato per evidenziare il fatto che siamo in impostazioni
                //fontFamily = pokemonPixelFont,
            )
        }
        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = { expandedState.value = false },
            modifier = Modifier
                .width(150.dp)
                .height(110.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem({ Text(option,/*color = if (option == "any") Color.Black else Color.Red  ,*/fontSize = 10.sp,fontFamily = pokemonPixelFont) },onClick = {
                    prevOption = selectedOption
                    selectedOption = option
                    showConfirmationDialog = true
                    expandedState.value = false
                })
            }
        }
        if(showConfirmationDialog) {
            ConfirmLanguageChangeDialog({
                onOptionSelected(selectedOption)
                showConfirmationDialog = false
            }, {
                Log.d("ChoiceLanguageMenu", selectedOption)
                selectedOption = prevOption
                showConfirmationDialog = false
            })
            expandedState.value = false
        }
    }
}

@Composable
fun ConfirmLanguageChangeDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(text = stringResource(R.string.change_language))
        },
        text = {
            Text(stringResource(R.string.change_language_disclaimer))
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
                onClick = onCancel
            ) {
                Text(stringResource(R.string.no))
            }
        }
    )
}
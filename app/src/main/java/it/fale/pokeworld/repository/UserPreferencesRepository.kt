package it.fale.pokeworld.repository

import android.content.Context
import android.content.SharedPreferences
import it.fale.pokeworld.utils.Language
import java.util.Locale

/**
 * Repository per le preferenze dell'utente.
 */
object UserPreferencesRepository {

    private const val PREFERENCES_FILE_NAME: String = "pokeworld_preferences"
    private const val PREFS_LANGUAGE_KEY: String = "language_preference"
    private const val PREFS_THEME_KEY: String = "dark_theme"
    private const val PREFS_FAVORITE_POKEMON_KEY: String ="favorite_pokemon"

    // Variabili per la gestione delle preferenze
    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Inizializza il repository di preferenze.
     */
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Salva la preferenza di tema in SharedPreferences.
     *
     * @param isDarkTheme Indica se il tema è scuro o meno.
     */
    fun saveDarkModePreference(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean(PREFS_THEME_KEY, isDarkTheme).apply()
    }

    /**
     * Ottiene la preferenza di tema da SharedPreferences.
     *
     * @param defValue Valore di default da utilizzare se non viene trovato nulla.
     * @return True se il tema è scuro, false altrimenti.
     */
    fun getDarkModePreference(defValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(PREFS_THEME_KEY, defValue)
    }

    /**
     * Salva la preferenza di lingua in SharedPreferences.
     *
     * @param language La lingua da salvare.
     */
    fun saveLanguagePreference(language: Language) {
        sharedPreferences.edit().putString(PREFS_LANGUAGE_KEY, language.code).apply()
    }

    /**
     * Ottiene la preferenza di lingua da SharedPreferences.
     *
     * @param defValue Valore di default da utilizzare se non viene trovato nulla.
     * @return La lingua corrente.
     */
    fun getLanguagePreference(defValue: String = Locale.getDefault().language): Language {
        return Language.fromCode(
            sharedPreferences.getString(
                PREFS_LANGUAGE_KEY,
                defValue
            )!!
        )
    }

    /**
     * Salva l'ID del pokemon preferito in SharedPreferences.
     *
     * @param pokemonId L'ID del pokemon da salvare.
     */
    fun saveFavoritePokemonId(pokemonId: Int) {
        sharedPreferences.edit().putInt(PREFS_FAVORITE_POKEMON_KEY, pokemonId).apply()
    }

    /**
     * Rimuove l'ID del pokemon preferito in SharedPreferences.
     */
    fun clearFavoritePokemonId() {
        sharedPreferences.edit().remove(PREFS_FAVORITE_POKEMON_KEY).apply()
    }

    /**
     * Ottiene l'ID del pokemon preferito da SharedPreferences.
     *
     * @return L'ID del pokemon preferito.
     */
    fun getFavoritePokemonId(): Int? {
        val id = sharedPreferences.getInt(PREFS_FAVORITE_POKEMON_KEY, -1)
        return if(id == -1) null else id
    }

}
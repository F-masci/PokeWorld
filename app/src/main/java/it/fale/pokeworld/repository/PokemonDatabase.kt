package it.fale.pokeworld.repository

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.fale.pokeworld.entity.AbilityEntity
import it.fale.pokeworld.entity.ItemEntity
import it.fale.pokeworld.entity.MoveEntity
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.cross.PokemonAbilityCross
import it.fale.pokeworld.entity.cross.PokemonItemCross
import it.fale.pokeworld.entity.cross.PokemonMoveCross

/**
 * Classe per gestire il database locale.
 */
@Database(entities = [
        PokemonEntity::class,
        AbilityEntity::class,
        PokemonAbilityCross::class,
        ItemEntity::class,
        PokemonItemCross::class,
        MoveEntity::class,
        PokemonMoveCross::class
     ],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase: RoomDatabase() {

    companion object {

        // Singola istanza del database
        private var db: PokemonDatabase? = null

        /**
         * Ottiene l'istanza del database.
         *
         * @param context Context dell'applicazione.
         * @return L'istanza del database.
         */
        fun getInstance(context: Context): PokemonDatabase {
            if (db == null) {
                Log.d("PokemonDatabase", "Creating new database instance")
                db = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon.db"
                )
                    .createFromAsset("pokemon.db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return db as PokemonDatabase
        }
    }

    /**
     * Ottiene il DAO per le operazioni di database.
     *
     * @return Il DAO per le operazioni di database.
     */
    abstract fun pokemonDao(): PokemonDao
}
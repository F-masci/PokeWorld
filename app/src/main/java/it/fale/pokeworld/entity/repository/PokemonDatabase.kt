package it.fale.pokeworld.entity.repository

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.fale.pokeworld.entity.AbilityEntity
import it.fale.pokeworld.entity.ItemEntity
import it.fale.pokeworld.entity.MoveEntity
import it.fale.pokeworld.entity.PokemonEntity
import it.fale.pokeworld.entity.PokemonTypeConverter
import it.fale.pokeworld.entity.cross.PokemonAbilityCross
import it.fale.pokeworld.entity.cross.PokemonItemCross
import it.fale.pokeworld.entity.cross.PokemonMoveCross

/**
 * Database room contenente le informazioni dei Pokemon
 * Realizzato come Singleton
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

        private var db: PokemonDatabase? = null

        fun getInstance(context: Context): PokemonDatabase {
            if (db == null) {
                Log.d("PokemonDatabase", "Creating new database instance")
                db = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon.db"
                )
                    .createFromAsset("pokemon.db")
                    .build()
            }
            return db as PokemonDatabase
        }
    }

    abstract fun pokemonDao(): PokemonDao
}
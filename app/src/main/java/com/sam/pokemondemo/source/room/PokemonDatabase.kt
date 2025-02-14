package com.sam.pokemondemo.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sam.pokemondemo.source.room.dao.CaptureDao
import com.sam.pokemondemo.source.room.dao.PokemonDao
import com.sam.pokemondemo.source.room.dao.RefDao
import com.sam.pokemondemo.source.room.dao.TypeDao
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef

@Database(
    entities = [
        PokemonEntity::class, TypeEntity::class, TypePokemonCrossRef::class,
        CaptureEntity::class,
    ],
    views = [
        CapturedPokemonView::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    JsonConverters::class,
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun typeDao(): TypeDao
    abstract fun refDao(): RefDao
    abstract fun captureDao(): CaptureDao

    companion object {
        @Volatile
        private var instance: PokemonDatabase? = null
        private val LOCK = Any()
        private const val DATABASE_NAME = "pokemon_database"

        fun getInstance(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PokemonDatabase::class.java,
            DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }
}

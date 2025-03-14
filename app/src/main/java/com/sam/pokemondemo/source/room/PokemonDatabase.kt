package com.sam.pokemondemo.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sam.pokemondemo.source.room.dao.CaptureDao
import com.sam.pokemondemo.source.room.dao.ImageCacheDao
import com.sam.pokemondemo.source.room.dao.PokemonDao
import com.sam.pokemondemo.source.room.dao.RefDao
import com.sam.pokemondemo.source.room.dao.TypeDao
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonView
import com.sam.pokemondemo.source.room.entity.ImageCacheEntity
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef

@Database(
    entities = [
        PokemonEntity::class, TypeEntity::class, TypePokemonCrossRef::class,
        CaptureEntity::class, ImageCacheEntity::class,
    ],
    views = [
        CapturedPokemonView::class, DetailPokemonView::class,
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
    abstract fun imageCacheDao(): ImageCacheDao
}

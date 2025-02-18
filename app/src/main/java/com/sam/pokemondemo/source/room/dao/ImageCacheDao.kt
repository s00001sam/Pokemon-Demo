package com.sam.pokemondemo.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sam.pokemondemo.source.room.entity.ImageCacheEntity

@Dao
interface ImageCacheDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(entity: ImageCacheEntity)

    @Query("SELECT imageUrl FROM imageCache")
    suspend fun getCacheList(): List<String>

    @Query("DELETE FROM imageCache")
    suspend fun clear()
}

package com.sam.pokemondemo.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sam.pokemondemo.source.room.entity.CaptureEntity

@Dao
interface CaptureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCapture(capture: CaptureEntity)

    @Query("DELETE FROM capture WHERE id = :id")
    suspend fun deleteCaptureById(id: Int)
}

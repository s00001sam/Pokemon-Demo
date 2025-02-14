package com.sam.pokemondemo.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: List<TypeEntity>)

    @Transaction
    @Query("SELECT * FROM type")
    fun getTypeWithPokemons(): Flow<List<TypeWithPokemons>>
}

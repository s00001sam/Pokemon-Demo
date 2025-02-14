package com.sam.pokemondemo.source.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef

@Dao
interface RefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(crossRefs: List<TypePokemonCrossRef>)

    @Query("DELETE FROM typePokemonCrossRef WHERE pokemonId = :pokemonId")
    suspend fun deleteRef(pokemonId: Int)
}

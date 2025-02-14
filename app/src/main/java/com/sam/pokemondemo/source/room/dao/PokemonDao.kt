package com.sam.pokemondemo.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.PokemonEntity

@Dao
interface PokemonDao {
    @Transaction
    @Upsert(entity = PokemonEntity::class)
    suspend fun upsertBasicLocalPokemons(basicList: List<BasicPokemonInfos>)

    @Query("SELECT name FROM pokemon")
    suspend fun getPokemonNames(): List<String>
}

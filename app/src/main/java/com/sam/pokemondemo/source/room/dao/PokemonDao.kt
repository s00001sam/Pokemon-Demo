package com.sam.pokemondemo.source.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonWithTypes
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Transaction
    @Upsert(entity = PokemonEntity::class)
    suspend fun upsertBasicLocalPokemons(basicList: List<BasicPokemonInfos>)

    @Query("SELECT name FROM pokemon")
    suspend fun getPokemonNames(): List<String>

    @Transaction
    @Query("SELECT * FROM capture_pokemon_view WHERE capturedTime IS NOT NULL ORDER BY capturedTime DESC")
    fun getCapturedPokemons(): Flow<List<CapturedPokemonView>>

    @Transaction
    @Upsert(entity = PokemonEntity::class)
    suspend fun upsertPokemonEntity(pokemonEntity: PokemonEntity)

    @Query("SELECT * FROM detail_pokemon_view WHERE pokemonId = :pokemonId")
    fun getDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes>
}

package com.sam.pokemondemo.source.datasource

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BaseDataSource {
    suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse>
    suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse>
    suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse>
    suspend fun getPokemonNames(): List<String>
    suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    )

    fun getTypeWithPokemons(): Flow<List<TypeWithPokemons>>
}

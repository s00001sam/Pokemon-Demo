package com.sam.pokemondemo.source.repo

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonWithTypes
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BaseRepository {
    suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse>
    suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse>
    suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse>
    suspend fun getLocalPokemonNames(): List<String>
    suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    )

    fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>>
    suspend fun insertCapture(capture: CaptureEntity)
    suspend fun deleteCaptureById(id: Int)
    fun getLocalCapturedPokemons(): Flow<List<CapturedPokemonView>>
    suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse>
    suspend fun updateDetails(
        pokemonEntity: PokemonEntity,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    )

    fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes>
}

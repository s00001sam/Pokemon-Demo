package com.sam.pokemondemo.source.datasource

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.apiservice.PokemonApiService
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
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: PokemonApiService,
) : BaseDataSource {
    override suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse> {
        return apiService.getBasicPokemons(url)
    }

    override suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse> {
        return apiService.getPokemon(url)
    }

    override suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse> {
        return apiService.getPokemon(id)
    }

    override suspend fun getLocalPokemonNames(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>
    ) {
        TODO("Not yet implemented")
    }

    override fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCapture(capture: CaptureEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCaptureById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getLocalCapturedPokemons(): Flow<List<CapturedPokemonView>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse> {
        return apiService.getPokemonSpecies(id)
    }

    override suspend fun updateDetails(
        pokemonEntity: PokemonEntity,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>
    ) {
        TODO("Not yet implemented")
    }

    override fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes> {
        TODO("Not yet implemented")
    }
}

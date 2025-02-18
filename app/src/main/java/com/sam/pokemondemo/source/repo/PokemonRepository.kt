package com.sam.pokemondemo.source.repo

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.datasource.BaseDataSource
import com.sam.pokemondemo.source.hilt.LocalData
import com.sam.pokemondemo.source.hilt.RemoteData
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

class PokemonRepository @Inject constructor(
    @LocalData private val localDataSource: BaseDataSource,
    @RemoteData private val remoteDataSource: BaseDataSource,
) : BaseRepository {
    override suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse> {
        return remoteDataSource.getRemoteBasicPokemons(url)
    }

    override suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse> {
        return remoteDataSource.getRemotePokemon(url)
    }

    override suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse> {
        return remoteDataSource.getRemotePokemon(id)
    }

    override suspend fun getLocalPokemonNames(): List<String> {
        return localDataSource.getLocalPokemonNames()
    }

    override suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>
    ) {
        localDataSource.upsertBasicPokemonsAndTypes(basicInfos, refs, types)
    }

    override fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        return localDataSource.getLocalTypeWithPokemons()
    }

    override suspend fun insertCapture(capture: CaptureEntity) {
        localDataSource.insertCapture(capture)
    }

    override suspend fun deleteCaptureById(id: Int) {
        localDataSource.deleteCaptureById(id)
    }

    override fun getLocalCapturedPokemonsByTimeDesc(): Flow<List<CapturedPokemonView>> {
        return localDataSource.getLocalCapturedPokemons()
    }

    override suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse> {
        return remoteDataSource.getRemotePokemonSpecies(id)
    }

    override suspend fun updateDetails(
        pokemonEntity: PokemonEntity,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    ) {
        localDataSource.updateDetails(pokemonEntity, refs, types)
    }

    override fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes> {
        return localDataSource.getLocalDetailWithTypes(pokemonId)
    }

    override suspend fun clearLocalDataWithoutCapture() {
        localDataSource.clearLocalDataWithoutCapture()
    }
}

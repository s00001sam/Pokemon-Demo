package com.sam.pokemondemo.source.repo

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.datasource.LocalDataSource
import com.sam.pokemondemo.source.datasource.RemoteDataSource
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonWithTypes
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : BaseRepository {
    override suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getRemoteBasicPokemons(url)
        }
    }

    override suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getRemotePokemon(url)
        }
    }

    override suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getRemotePokemon(id)
        }
    }

    override suspend fun getLocalPokemonNames(): List<String> {
        return withContext(Dispatchers.IO) {
            localDataSource.getLocalPokemonNames()
        }
    }

    override suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    ) {
        withContext(Dispatchers.IO) {
            localDataSource.upsertBasicPokemonsAndTypes(basicInfos, refs, types)
        }
    }

    override fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        return localDataSource.getLocalTypeWithPokemons()
    }

    override suspend fun insertCapture(capture: CaptureEntity) {
        withContext(Dispatchers.IO) {
            localDataSource.insertCapture(capture)
        }
    }

    override suspend fun deleteCaptureById(id: Int) {
        withContext(Dispatchers.IO) {
            localDataSource.deleteCaptureById(id)
        }
    }

    override fun getLocalCapturedPokemonsByTimeDesc(): Flow<List<CapturedPokemonView>> {
        return localDataSource.getLocalCapturedPokemons()
    }

    override suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getRemotePokemonSpecies(id)
        }
    }

    override suspend fun updateDetails(
        pokemonEntity: PokemonEntity,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    ) {
        withContext(Dispatchers.IO) {
            localDataSource.updateDetails(pokemonEntity, refs, types)
        }
    }

    override fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes> {
        return localDataSource.getLocalDetailWithTypes(pokemonId)
    }

    override suspend fun clearLocalDataWithoutCapture() {
        withContext(Dispatchers.IO) {
            localDataSource.clearLocalDataWithoutCapture()
        }
    }
}

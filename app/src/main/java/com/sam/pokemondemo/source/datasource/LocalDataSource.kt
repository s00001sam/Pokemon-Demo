package com.sam.pokemondemo.source.datasource

import androidx.room.withTransaction
import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.room.PokemonDatabase
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

class LocalDataSource @Inject constructor(
    private val db: PokemonDatabase,
) : BaseDataSource {
    override suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalPokemonNames(): List<String> {
        return db.pokemonDao().getPokemonNames()
    }

    override suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    ) {
        db.withTransaction {
            db.refDao().insertList(refs)
            db.typeDao().insertList(types)
            db.pokemonDao().upsertBasicLocalPokemons(basicInfos)
        }
    }

    override fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        return db.typeDao().getTypeWithPokemons()
    }

    override suspend fun insertCapture(capture: CaptureEntity) {
        db.captureDao().insertCapture(capture)
    }

    override suspend fun deleteCaptureById(id: Int) {
        db.captureDao().deleteCaptureById(id)
    }

    override fun getLocalCapturedPokemons(): Flow<List<CapturedPokemonView>> {
        return db.pokemonDao().getCapturedPokemons()
    }

    override suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDetails(
        pokemonEntity: PokemonEntity,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>,
    ) {
        db.withTransaction {
            db.refDao().deleteRef(pokemonEntity.id)
            db.refDao().insertList(refs)
            db.typeDao().insertList(types)
            db.pokemonDao().upsertPokemonEntity(pokemonEntity)
        }
    }

    override fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes> {
        return db.pokemonDao().getDetailWithTypes(pokemonId)
    }
}

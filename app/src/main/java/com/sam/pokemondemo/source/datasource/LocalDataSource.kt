package com.sam.pokemondemo.source.datasource

import androidx.room.withTransaction
import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.room.PokemonDatabase
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
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

    override suspend fun getPokemonNames(): List<String> {
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

    override fun getTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        return db.typeDao().getTypeWithPokemons()
    }
}

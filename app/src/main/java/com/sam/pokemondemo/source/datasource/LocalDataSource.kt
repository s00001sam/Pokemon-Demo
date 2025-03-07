package com.sam.pokemondemo.source.datasource

import androidx.room.withTransaction
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
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val db: PokemonDatabase,
) {
    suspend fun getLocalPokemonNames(): List<String> {
        return db.pokemonDao().getPokemonNames()
    }

    suspend fun upsertBasicPokemonsAndTypes(
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

    fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        return db.typeDao().getTypeWithPokemons()
    }

    suspend fun insertCapture(capture: CaptureEntity) {
        db.captureDao().insertCapture(capture)
    }

    suspend fun deleteCaptureById(id: Int) {
        db.captureDao().deleteCaptureById(id)
    }

    fun getLocalCapturedPokemons(): Flow<List<CapturedPokemonView>> {
        return db.pokemonDao().getCapturedPokemons()
    }

    suspend fun updateDetails(
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

    fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes> {
        return db.pokemonDao().getDetailWithTypes(pokemonId)
    }

    suspend fun clearLocalDataWithoutCapture() {
        db.withTransaction {
            db.pokemonDao().clear()
            db.typeDao().clear()
            db.refDao().clear()
        }
    }
}

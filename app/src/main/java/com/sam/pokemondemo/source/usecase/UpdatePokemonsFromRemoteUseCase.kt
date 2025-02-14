package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.model.State
import com.sam.pokemondemo.source.apiservice.PokemonApiService.Companion.DEFAULT_GET_POKEMONS_URL
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos.Companion.getBasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.TypeEntity.Companion.toTypeEntities
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef.Companion.toTypePokemonCrossRefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdatePokemonsFromRemoteUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    fun invoke(): Flow<State<Any>> = flow {
        runCatching {
            val remoteBasicResults = withContext(Dispatchers.IO) {
                val response = repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL)
                response.body()?.results.orEmpty()
            }

            remoteBasicResults.chunked(BATCH_SIZE).forEach { batchResults ->
                supervisorScope {
                    batchResults.map { result ->
                        async(Dispatchers.IO) {
                            runCatching {
                                if (result.url == null) return@async null

                                repo.getRemotePokemon(url = result.url).body()
                            }
                                .onFailure(Timber::e)
                                .getOrNull()
                        }
                    }.awaitAll() // 等待這一批全部完成
                }.filterNotNull() // 過濾掉失敗的請求
                    .also { remotePokemon ->
                        val basicInfos = remotePokemon.map { it.getBasicPokemonInfos() }
                        val refs = remotePokemon.toTypePokemonCrossRefs()
                        val types = remotePokemon.flatMap { it.types.toTypeEntities() }

                        // 將這一批數據存入資料庫
                        withContext(Dispatchers.IO) {
                            repo.upsertBasicPokemonsAndTypes(
                                basicInfos = basicInfos,
                                refs = refs,
                                types = types,
                            )
                        }
                    }
            }

            emit(State.Success(data = Any()))
        }.onFailure {
            Timber.e(it)
            emit(State.Error(it))
        }
    }
        .flowOn(Dispatchers.IO)
        .onStart {
            emit(State.Loading)
        }

    companion object {
        // 每次同時處理的 API 數量
        private const val BATCH_SIZE = 10
    }
}

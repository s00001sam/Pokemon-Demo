package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.handleResponseError
import com.sam.pokemondemo.model.State
import com.sam.pokemondemo.source.apiservice.PokemonApiService.Companion.DEFAULT_GET_POKEMONS_URL
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
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
    private val imagePreloader: ImagePreloader,
) {
    /**
     * @param isFirstTimeLoadFinished If the first load is not finished, proceed with the process
     */
    fun invoke(
        isFirstTimeLoadFinished: Boolean,
    ): Flow<State<Any>> = flow {
        runCatching {
            val remoteBasicResults = withContext(Dispatchers.IO) {
                val response = repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL)
                response.handleResponseError()
                response.body()?.results.orEmpty()
            }

            /**
             * If the initial load is incomplete, need to know what has already been loaded
             */
            val firstTimeLoadedNames: List<String> = when (isFirstTimeLoadFinished) {
                true -> emptyList()
                false -> repo.getLocalPokemonNames()
            }

            remoteBasicResults.chunked(BATCH_SIZE).forEach { batchResults ->
                supervisorScope {
                    batchResults.map { result ->
                        async(Dispatchers.IO) {
                            runCatching {
                                if (result.url == null || result.name == null) return@async null
                                if (firstTimeLoadedNames.contains(result.name)) return@async null

                                repo.getRemotePokemon(url = result.url).also {
                                    it.handleResponseError()
                                }.body()
                            }
                                .onFailure(Timber::e)
                                .getOrNull()
                        }
                    }.awaitAll() // Wait for this batch to complete
                }.filterNotNull() // Filter out failed requests
                    .also { remotePokemon ->
                        val basicInfos = remotePokemon.map { it.getBasicPokemonInfos() }
                        val refs = remotePokemon.toTypePokemonCrossRefs()
                        val types = remotePokemon.flatMap { it.types.toTypeEntities() }

                        withContext(Dispatchers.IO) {
                            // Preload All Images for cache
                            imagePreloader.load(basicInfos.map { it.imageUrl })
                            // Store this batch of data in the database
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
        // Number of APIs processed at the same time
        private const val BATCH_SIZE = 10
    }
}

package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.handleResponseError
import com.sam.pokemondemo.model.State
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity.Companion.toTypeEntities
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UpdatePokemonDetailFromRemoteUseCase @Inject constructor(
    private val repo: BaseRepository,
    private val imagePreloader: ImagePreloader,
) {
    fun invoke(pokemonId: Int): Flow<State<Any>> = flow {
        runCatching {
            val basicRemotePokemon = CoroutineScope(Dispatchers.IO).async {
                repo.getRemotePokemon(pokemonId).also {
                    it.handleResponseError()
                }
            }.await().body()
            val species = CoroutineScope(Dispatchers.IO).async {
                repo.getRemotePokemonSpecies(pokemonId).also {
                    it.handleResponseError()
                }
            }.await().body()
            val pokemon = PokemonEntity(
                id = basicRemotePokemon?.id ?: 0,
                name = basicRemotePokemon?.name.orEmpty(),
                imageUrl = basicRemotePokemon?.sprites?.other?.officialArtwork?.frontDefault.orEmpty(),
                evolvesFromName = species?.evolvesFromSpecies?.name.orEmpty(),
                description = species?.flavorTextEntries?.getOrNull(0)?.flavorText.orEmpty(),
            )
            val types = basicRemotePokemon?.types.toTypeEntities()
            val refs = types.mapNotNull { type ->
                val id = basicRemotePokemon?.id ?: return@mapNotNull null
                TypePokemonCrossRef(typeName = type.name, pokemonId = id)
            }

            withContext(Dispatchers.IO) {
                // Preload image
                imagePreloader.load(listOf(pokemon.imageUrl))
                // Store details in the database
                repo.updateDetails(pokemon, refs, types)
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
}

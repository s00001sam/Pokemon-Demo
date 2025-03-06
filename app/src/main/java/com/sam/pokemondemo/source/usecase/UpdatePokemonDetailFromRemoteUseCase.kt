package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.handleResponseError
import com.sam.pokemondemo.model.State
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity.Companion.toTypeEntities
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject

class UpdatePokemonDetailFromRemoteUseCase @Inject constructor(
    private val repo: BaseRepository,
    private val imagePreloader: ImagePreloader,
) {
    fun invoke(pokemonId: Int): Flow<State<Any>> = flow {
        runCatching {
            val basicRemotePokemon = repo.getRemotePokemon(pokemonId).also {
                it.handleResponseError()
            }.body()
            val species = repo.getRemotePokemonSpecies(pokemonId).also {
                it.handleResponseError()
            }.body()

            val pokemon = PokemonEntity(
                id = basicRemotePokemon?.id ?: 0,
                name = basicRemotePokemon?.name.orEmpty(),
                imageUrl = basicRemotePokemon?.sprites?.other?.officialArtwork?.frontDefault.orEmpty(),
                evolvesFromName = species?.evolvesFromSpecies?.name.orEmpty(),
                description = species?.getDescription().orEmpty(),
            )
            val types = basicRemotePokemon?.types.toTypeEntities()
            val refs = types.mapNotNull { type ->
                val id = basicRemotePokemon?.id ?: return@mapNotNull null
                TypePokemonCrossRef(typeName = type.name, pokemonId = id)
            }

            // Preload image
            imagePreloader.load(listOf(pokemon.imageUrl))
            // Store details in the database
            repo.updateDetails(pokemon, refs, types)

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

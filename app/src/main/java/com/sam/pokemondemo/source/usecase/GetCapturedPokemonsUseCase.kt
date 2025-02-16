package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.model.CapturedDisplayPokemon
import com.sam.pokemondemo.model.CapturedDisplayPokemon.Companion.toDisplayPokemon
import com.sam.pokemondemo.source.repo.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCapturedPokemonsUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    fun invoke(): Flow<List<CapturedDisplayPokemon>> {
        return repo.getLocalCapturedPokemonsByTimeDesc().map {
            it.map { it.toDisplayPokemon() }
        }
            .flowOn(Dispatchers.IO)
    }
}

package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.model.DisplayTypeWithPokemons
import com.sam.pokemondemo.model.DisplayTypeWithPokemons.Companion.toDisplayTypeWithPokemons
import com.sam.pokemondemo.source.repo.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTypeWithPokemonsUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    fun invoke(): Flow<List<DisplayTypeWithPokemons>> {
        return repo.getTypeWithPokemons().map {
            it.map { it.toDisplayTypeWithPokemons() }
        }
            .flowOn(Dispatchers.IO)
    }
}

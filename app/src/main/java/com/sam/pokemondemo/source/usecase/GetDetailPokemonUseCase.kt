package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.model.DetailDisplayPokemon
import com.sam.pokemondemo.model.DetailDisplayPokemon.Companion.toDisplayPokemon
import com.sam.pokemondemo.source.repo.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetDetailPokemonUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    fun invoke(pokemonId: Int): Flow<DetailDisplayPokemon> {
        return repo.getLocalDetailWithTypes(pokemonId).mapNotNull {
            it.toDisplayPokemon()
        }
            .flowOn(Dispatchers.IO)
    }
}

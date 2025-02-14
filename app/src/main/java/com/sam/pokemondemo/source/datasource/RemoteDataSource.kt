package com.sam.pokemondemo.source.datasource

import com.sam.pokemondemo.source.apiservice.PokemonApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: PokemonApiService,
) : BaseDataSource {
}

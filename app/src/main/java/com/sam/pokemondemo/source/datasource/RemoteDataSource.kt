package com.sam.pokemondemo.source.datasource

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.apiservice.PokemonApiService
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: PokemonApiService,
) {
    suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse> {
        return apiService.getBasicPokemons(url)
    }

    suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse> {
        return apiService.getPokemon(url)
    }

    suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse> {
        return apiService.getPokemon(id)
    }

    suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse> {
        return apiService.getPokemonSpecies(id)
    }
}

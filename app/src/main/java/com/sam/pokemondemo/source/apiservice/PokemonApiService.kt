package com.sam.pokemondemo.source.apiservice

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokemonApiService {
    /**
     * Since the Response's next field is a url,
     *
     * and considering the potential need to retrieve data from multiple pages in the future,
     *
     * url is currently being used
     */
    @GET
    suspend fun getBasicPokemons(
        @Url url: String,
    ): Response<BasicPokemonsResponse>

    @GET
    suspend fun getPokemon(
        @Url url: String,
    ): Response<RemotePokemonResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): Response<RemotePokemonResponse>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): Response<PokemonSpeciesResponse>

    companion object {
        const val PAGE_COUNT = 151
        const val DEFAULT_GET_POKEMONS_URL = "https://pokeapi.co/api/v2/pokemon?limit=$PAGE_COUNT"
    }
}

package com.sam.pokemondemo.source.apiservice

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokemonApiService {
    /**
     * 考量後續可能需要做多頁的資料拿取，使用 Url
     *
     *  因為 Response 的 next 也是 url
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

    companion object {
        const val PAGE_COUNT = 151
        const val DEFAULT_GET_POKEMONS_URL = "https://pokeapi.co/api/v2/pokemon?limit=$PAGE_COUNT"
    }
}

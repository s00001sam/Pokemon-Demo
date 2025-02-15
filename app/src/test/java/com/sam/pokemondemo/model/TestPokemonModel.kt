package com.sam.pokemondemo.model


data class TestPokemonModel(
    val url: String = "",
    val id: Int = -1,
    val name: String = "",
    val imageUrl: String = "",
    val evolvesFromName: String = "",
    val description: String = "",
)

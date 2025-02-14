package com.sam.pokemondemo.source.room.entity

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "capture_pokemon_view",
    value = """
             SELECT c.id, p.id AS pokemonId, p.name, p.imageUrl, c.capturedTime
             FROM capture c 
             INNER JOIN pokemon p ON p.id = c.pokemonId
        """,
)
data class CapturedPokemonView(
    val id: Int = -1,
    val pokemonId: Int = -1,
    val name: String = "",
    val imageUrl: String = "",
    val capturedTime: Long? = null,
)

package com.sam.pokemondemo.model

import com.sam.pokemondemo.source.room.entity.PokemonEntity

interface DisplayPokemon {
    val pokemonId: Int
    val name: String
    val imageUrl: String
}

data class BasicDisplayPokemon(
    override val pokemonId: Int = -1,
    override val name: String = "",
    override val imageUrl: String = "",
) : DisplayPokemon {
    companion object {
        fun PokemonEntity.toDisplayPokemon(): BasicDisplayPokemon {
            return BasicDisplayPokemon(
                pokemonId = id,
                name = name,
                imageUrl = imageUrl,
            )
        }
    }
}

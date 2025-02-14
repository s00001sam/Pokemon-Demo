package com.sam.pokemondemo.model

import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonWithTypes
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

data class CapturedDisplayPokemon(
    override val pokemonId: Int = -1,
    override val name: String = "",
    override val imageUrl: String = "",
    val captureId: Int = -1,
    val capturedTime: Long? = null,
) : DisplayPokemon {
    companion object {
        fun CapturedPokemonView.toDisplayPokemon(): CapturedDisplayPokemon {
            return CapturedDisplayPokemon(
                captureId = id,
                pokemonId = pokemonId,
                name = name,
                imageUrl = imageUrl,
                capturedTime = capturedTime,
            )
        }
    }
}

data class DetailDisplayPokemon(
    override val pokemonId: Int = -1,
    override val name: String = "",
    override val imageUrl: String = "",
    val evolvesFromId: Int = -1,
    val evolvesFromName: String = "",
    val evolvesFromImageUrl: String = "",
    val description: String = "",
    val typeNames: List<String> = emptyList(),
) : DisplayPokemon {
    companion object {
        fun DetailPokemonWithTypes.toDisplayPokemon(): DetailDisplayPokemon {
            return DetailDisplayPokemon(
                pokemonId = pokemon.pokemonId,
                name = pokemon.name,
                imageUrl = pokemon.imageUrl,
                evolvesFromId = pokemon.evolvesFromId,
                evolvesFromName = pokemon.evolvesFromName,
                evolvesFromImageUrl = pokemon.evolvesFromImageUrl,
                description = pokemon.description,
                typeNames = typeRefs.map { it.typeName },
            )
        }
    }
}

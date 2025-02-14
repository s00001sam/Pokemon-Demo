package com.sam.pokemondemo.source.room.entity

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation

data class DetailPokemonWithTypes(
    @Embedded val pokemon: DetailPokemonView,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonId",
    )
    val typeRefs: List<TypePokemonCrossRef> = emptyList(),
)

@DatabaseView(
    viewName = "detail_pokemon_view",
    value = """
             SELECT p.id AS pokemonId, p.name, p.imageUrl, p.description, p.evolvesFromName,
                COALESCE(evolves.id, -1) AS evolvesFromId,
                COALESCE(evolves.imageUrl, '') AS evolvesFromImageUrl
             FROM pokemon p
             LEFT JOIN pokemon evolves ON p.evolvesFromName = evolves.name
             LEFT JOIN typePokemonCrossRef ref ON p.id = ref.pokemonId
        """,
)
data class DetailPokemonView(
    val pokemonId: Int = -1,
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val evolvesFromId: Int = -1,
    val evolvesFromName: String = "",
    val evolvesFromImageUrl: String = "",
)

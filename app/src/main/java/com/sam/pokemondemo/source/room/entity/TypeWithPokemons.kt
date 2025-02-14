package com.sam.pokemondemo.source.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TypeWithPokemons(
    @Embedded val type: TypeEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "id",
        associateBy = Junction(
            TypePokemonCrossRef::class,
            parentColumn = "typeName",
            entityColumn = "pokemonId",
        ),
    )
    val pokemons: List<PokemonEntity> = emptyList(),
)

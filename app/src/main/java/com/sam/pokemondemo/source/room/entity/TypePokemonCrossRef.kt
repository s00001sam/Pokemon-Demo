package com.sam.pokemondemo.source.room.entity

import androidx.room.Entity
import com.sam.pokemondemo.model.RemotePokemonResponse

@Entity(
    tableName = "typePokemonCrossRef",
    primaryKeys = ["typeName", "pokemonId"],
)
data class TypePokemonCrossRef(
    val typeName: String,
    val pokemonId: Int,
) {
    companion object {
        fun List<RemotePokemonResponse>.toTypePokemonCrossRefs(): List<TypePokemonCrossRef> {
            return this.flatMap { pokemon ->
                pokemon.types.orEmpty().mapNotNull { type ->
                    val pokemonId = pokemon.id ?: return@mapNotNull null
                    val typeName = type.type?.name ?: return@mapNotNull null
                    TypePokemonCrossRef(
                        pokemonId = pokemonId,
                        typeName = typeName,
                    )
                }
            }
        }
    }
}

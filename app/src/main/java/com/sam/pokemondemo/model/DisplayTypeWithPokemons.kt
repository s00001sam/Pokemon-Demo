package com.sam.pokemondemo.model

import com.sam.pokemondemo.model.BasicDisplayPokemon.Companion.toDisplayPokemon
import com.sam.pokemondemo.model.DisplayType.Companion.toDisplayType
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons

data class DisplayTypeWithPokemons(
    val type: DisplayType,
    val pokemons: List<BasicDisplayPokemon>,
) {
    companion object {
        fun TypeWithPokemons.toDisplayTypeWithPokemons(): DisplayTypeWithPokemons {
            return DisplayTypeWithPokemons(
                type = type.toDisplayType(),
                pokemons = pokemons.map { it.toDisplayPokemon() },
            )
        }
    }
}

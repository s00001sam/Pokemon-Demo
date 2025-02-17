package com.sam.pokemondemo.ui.preview.model

import com.sam.pokemondemo.model.DisplayPokemon

/**
 * Model for preview
 */
data class TypeWithPokemonsModel(
    val typeName: String,
    val pokemons: List<DisplayPokemon>,
    val isBottomLineVisible: Boolean = false,
)

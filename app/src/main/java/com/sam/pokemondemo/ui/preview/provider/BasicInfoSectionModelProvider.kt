package com.sam.pokemondemo.ui.preview.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.sam.pokemondemo.model.DetailDisplayPokemon
import com.sam.pokemondemo.ui.preview.model.BasicInfoSectionModel

class BasicInfoSectionModelProvider : PreviewParameterProvider<BasicInfoSectionModel> {
    override val values: Sequence<BasicInfoSectionModel>
        get() = sequenceOf(
            BasicInfoSectionModel(
                DetailDisplayPokemon(
                    pokemonId = 1,
                    name = "pokemon1",
                    imageUrl = "",
                    typeNames = List(2) { "type$it" }
                ),
            ),
            BasicInfoSectionModel(
                DetailDisplayPokemon(
                    pokemonId = 2,
                    name = "pokemon2",
                    imageUrl = "",
                    typeNames = List(4) { "type$it" }
                ),
            ),
            BasicInfoSectionModel(
                DetailDisplayPokemon(
                    pokemonId = 3,
                    name = "pokemon3",
                    imageUrl = "",
                    typeNames = List(20) { "type$it" }
                ),
            ),
        )
}

package com.sam.pokemondemo.ui.preview.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.sam.pokemondemo.model.BasicDisplayPokemon
import com.sam.pokemondemo.ui.preview.model.HomePokemonItemModel

class HomePokemonItemModelProvider : PreviewParameterProvider<HomePokemonItemModel> {
    override val values: Sequence<HomePokemonItemModel>
        get() = sequenceOf(
            HomePokemonItemModel(
                BasicDisplayPokemon(
                    pokemonId = 1,
                    name = "pokemon1",
                    imageUrl = "",
                ),
            ),
            HomePokemonItemModel(
                BasicDisplayPokemon(
                    pokemonId = 2,
                    name = "pokemon1".repeat(20),
                    imageUrl = "",
                ),
            ),
        )
}

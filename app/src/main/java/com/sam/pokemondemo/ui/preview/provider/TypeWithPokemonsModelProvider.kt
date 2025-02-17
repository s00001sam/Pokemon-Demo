package com.sam.pokemondemo.ui.preview.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.sam.pokemondemo.model.BasicDisplayPokemon
import com.sam.pokemondemo.ui.preview.model.TypeWithPokemonsModel

class TypeWithPokemonsModelProvider : PreviewParameterProvider<TypeWithPokemonsModel> {
    val pokemon = BasicDisplayPokemon(
        pokemonId = 1,
        name = "pokemon",
        imageUrl = "",
    )
    override val values: Sequence<TypeWithPokemonsModel>
        get() = sequenceOf(
            TypeWithPokemonsModel(
                typeName = "typeName",
                pokemons = List(10) { index ->
                    BasicDisplayPokemon(
                        pokemonId = index,
                        name = "pokemon$index",
                        imageUrl = "",
                    )
                },
                isBottomLineVisible = false,
            ),
            TypeWithPokemonsModel(
                typeName = "typeName",
                pokemons = List(10) { index ->
                    BasicDisplayPokemon(
                        pokemonId = index,
                        name = "pokemon$index",
                        imageUrl = "",
                    )
                },
                isBottomLineVisible = true,
            ),
            TypeWithPokemonsModel(
                typeName = "typeName".repeat(20),
                pokemons = List(10) { index ->
                    BasicDisplayPokemon(
                        pokemonId = index,
                        name = "pokemon$index",
                        imageUrl = "",
                    )
                },
                isBottomLineVisible = false,
            ),
        )
}

package com.sam.pokemondemo.source

import com.sam.pokemondemo.model.TestPokemonModel
import com.sam.pokemondemo.model.TestTypeModel

val mockTypes = listOf(
    TestTypeModel(name = "type1", url = "https://com.sam.pokemon/type1"),
    TestTypeModel(name = "type2", url = "https://com.sam.pokemon/type2"),
    TestTypeModel(name = "type3", url = "https://com.sam.pokemon/type3"),
    TestTypeModel(name = "type4", url = "https://com.sam.pokemon/type4"),
)

val mockPokemons = (1..22).mapIndexed { _, index ->
    val evolvesFromName = if (index % 4 == 1) "pokemon${index - 1}" else ""
    TestPokemonModel(
        id = index,
        url = "https://com.sam.pokemon/$index",
        name = "pokemon$index",
        imageUrl = "https://com.sam.pokemon/image$index",
        evolvesFromName = evolvesFromName,
        description = "Hello! I am pokemon$index.",
    )
}

val typePokemonMap: Map<String, List<TestPokemonModel>> = mapOf(
    mockTypes[0].name to (1..10).map { mockPokemons[it - 1] },
    mockTypes[1].name to (5..15).map { mockPokemons[it - 1] },
    mockTypes[2].name to (10..20).map { mockPokemons[it - 1] },
    mockTypes[3].name to (15..22).map { mockPokemons[it - 1] },
)

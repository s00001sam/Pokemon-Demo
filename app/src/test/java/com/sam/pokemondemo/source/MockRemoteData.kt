package com.sam.pokemondemo.source

import com.sam.pokemondemo.model.BaseResponse
import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.OfficialArtwork
import com.sam.pokemondemo.model.Other
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.model.SpritesResponse

const val mockPokemon1Name = "Pokemon1"
const val mockPokemon1Image = "https://com.sam.pokemon/image1"

val mockRemotePokemonResponseForDetail = RemotePokemonResponse(
    id = 1,
    name = mockPokemon1Name,
    sprites = SpritesResponse(
        other = Other(
            officialArtwork = OfficialArtwork(
                frontDefault = mockPokemon1Image
            )
        )
    ),
    types = emptyList(),
)

val mockPokemon1SpeciesResponseForDetail = PokemonSpeciesResponse(
    id = 1,
    evolvesFromSpecies = null,
    flavorTextEntries = emptyList(),
)

val mockBasicPokemonsResponse = BasicPokemonsResponse(
    count = 12,
    next = null,
    previous = null,
    results = listOf(
        BaseResponse(name = "Pokemon1", url = "url1"),
        BaseResponse(name = "Pokemon2", url = "url2"),
        BaseResponse(name = "Pokemon3", url = "url3"),
        BaseResponse(name = "Pokemon4", url = "url4"),
        BaseResponse(name = "Pokemon5", url = "url5"),
        BaseResponse(name = "Pokemon6", url = "url6"),
        BaseResponse(name = "Pokemon7", url = "url7"),
        BaseResponse(name = "Pokemon8", url = "url8"),
        BaseResponse(name = "Pokemon9", url = "url9"),
        BaseResponse(name = "Pokemon10", url = "url10"),
        BaseResponse(name = "Pokemon11", url = "url11"),
        BaseResponse(name = "Pokemon12", url = "url12"),
    ),
)

val mockRemotePokemonResponses = listOf(
    RemotePokemonResponse(
        id = 1,
        name = "Pokemon1",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image1",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 2,
        name = "Pokemon2",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image2",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 3,
        name = "Pokemon3",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image3",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 4,
        name = "Pokemon4",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image4",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 5,
        name = "Pokemon5",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image5",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 6,
        name = "Pokemon6",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image6",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 7,
        name = "Pokemon7",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image7",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 8,
        name = "Pokemon8",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image8",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 9,
        name = "Pokemon9",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image9",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 10,
        name = "Pokemon10",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image10",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 11,
        name = "Pokemon11",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image11",
                )
            )
        ),
        types = emptyList(),
    ),
    RemotePokemonResponse(
        id = 12,
        name = "Pokemon12",
        sprites = SpritesResponse(
            other = Other(
                officialArtwork = OfficialArtwork(
                    frontDefault = "https://com.sam.pokemon/image12",
                )
            )
        ),
        types = emptyList(),
    ),
)

val mockUrlToPokemonMap = mockBasicPokemonsResponse.results.orEmpty().mapNotNull { it.url }
    .zip(mockRemotePokemonResponses)
    .toMap()

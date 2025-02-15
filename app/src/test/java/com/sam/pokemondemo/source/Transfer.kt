package com.sam.pokemondemo.source

import com.sam.pokemondemo.model.BaseResponse
import com.sam.pokemondemo.model.RemoteType
import com.sam.pokemondemo.model.TestPokemonModel
import com.sam.pokemondemo.model.TestTypeModel
import com.sam.pokemondemo.model.TypeResponse
import com.sam.pokemondemo.source.room.entity.PokemonEntity

fun List<TestPokemonModel>.toBaseResponses(): List<BaseResponse> {
    return this.map {
        BaseResponse(name = it.name, url = it.url)
    }
}

fun List<TestTypeModel>.toTypeResponses(): List<TypeResponse> {
    return this.map {
        TypeResponse(
            type = RemoteType(name = it.name, url = it.url),
        )
    }
}

fun List<TestPokemonModel>.toPokemonEntities(): List<PokemonEntity> {
    return this.map {
        PokemonEntity(
            id = it.id,
            name = it.name,
            imageUrl = it.imageUrl,
            evolvesFromName = it.evolvesFromName,
            description = it.description,
        )
    }
}

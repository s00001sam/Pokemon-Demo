package com.sam.pokemondemo.source

import com.sam.pokemondemo.model.BaseResponse
import com.sam.pokemondemo.model.RemoteType
import com.sam.pokemondemo.model.TestPokemonModel
import com.sam.pokemondemo.model.TestTypeModel
import com.sam.pokemondemo.model.TypeResponse
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity

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

fun List<TestTypeModel>.toTypeEntities(): List<TypeEntity> {
    return this.map {
        TypeEntity(
            name = it.name,
        )
    }
}

fun TestPokemonModel.toPokemonEntity(): PokemonEntity {
    return PokemonEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        evolvesFromName = this.evolvesFromName,
        description = this.description,
    )
}

fun List<TestPokemonModel>.toPokemonEntities(): List<PokemonEntity> {
    return this.map { it.toPokemonEntity() }
}

fun List<TestPokemonModel>.toBasicPokemonEntities(): List<PokemonEntity> {
    return this.map {
        PokemonEntity(
            id = it.id,
            name = it.name,
            imageUrl = it.imageUrl,
        )
    }
}

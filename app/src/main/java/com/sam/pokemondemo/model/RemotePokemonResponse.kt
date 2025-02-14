package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemotePokemonResponse(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "sprites") val sprites: SpritesResponse? = null,
    @Json(name = "types") val types: List<TypeResponse>? = null,
) : Parcelable

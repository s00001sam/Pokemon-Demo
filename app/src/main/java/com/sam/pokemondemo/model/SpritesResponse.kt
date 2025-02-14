package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpritesResponse(
    @Json(name = "other") val other: Other? = null,
) : Parcelable

@Parcelize
data class Other(
    @Json(name = "official-artwork") val officialArtwork: OfficialArtwork? = null,
) : Parcelable

@Parcelize
data class OfficialArtwork(
    @Json(name = "front_default") val frontDefault: String? = null,
) : Parcelable

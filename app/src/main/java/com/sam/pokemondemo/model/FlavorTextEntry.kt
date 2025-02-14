package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlavorTextEntry(
    @Json(name = "flavor_text") val flavorText: String? = null,
    @Json(name = "language") val language: BaseResponse? = null,
    @Json(name = "version") val version: BaseResponse? = null,
) : Parcelable

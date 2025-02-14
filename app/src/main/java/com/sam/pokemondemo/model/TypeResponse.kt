package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypeResponse(
    @Json(name = "type") val type: RemoteType? = null,
) : Parcelable

@Parcelize
data class RemoteType(
    @Json(name = "name") val name: String? = null,
    @Json(name = "url") val url: String? = null,
) : Parcelable

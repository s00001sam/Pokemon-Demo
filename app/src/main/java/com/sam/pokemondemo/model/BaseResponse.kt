package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseResponse(
    @Json(name = "name") val name: String? = null,
    @Json(name = "url") val url: String? = null,
) : Parcelable

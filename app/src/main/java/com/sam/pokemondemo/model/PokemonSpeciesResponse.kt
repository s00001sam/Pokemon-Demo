package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonSpeciesResponse(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "evolves_from_species") val evolvesFromSpecies: BaseResponse? = null,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>? = null,
) : Parcelable

package com.sam.pokemondemo.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class PokemonSpeciesResponse(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "evolves_from_species") val evolvesFromSpecies: BaseResponse? = null,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<FlavorTextEntry>? = null,
) : Parcelable {
    fun getDescription(): String {
        val locale = Locale.getDefault()
        val myFlavorTextEntry = flavorTextEntries
            ?.find { it.language?.name.orEmpty().contains(locale.language) }
        val enFlavorTextEntry = flavorTextEntries
            ?.find { it.language?.name.orEmpty().contains(Locale.US.language) }
        return when {
            myFlavorTextEntry != null -> myFlavorTextEntry.flavorText.orEmpty()

            enFlavorTextEntry != null -> enFlavorTextEntry.flavorText.orEmpty()

            flavorTextEntries.isNullOrEmpty() -> ""

            else -> {
                flavorTextEntries.first().flavorText.orEmpty()
            }
        }
    }
}

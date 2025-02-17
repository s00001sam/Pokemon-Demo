package com.sam.pokemondemo.ui.preview.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.sam.pokemondemo.ui.preview.model.EvolveSectionModel

class EvolveSectionModelProvider : PreviewParameterProvider<EvolveSectionModel> {
    override val values: Sequence<EvolveSectionModel>
        get() = sequenceOf(
            EvolveSectionModel(
                evolvesFromId = 1,
                evolvesFromName = "test Name",
                evolvesFromImageUrl = "",
            ),
            EvolveSectionModel(
                evolvesFromId = 1,
                evolvesFromName = "test Name".repeat(20),
                evolvesFromImageUrl = "",
            ),
        )
}

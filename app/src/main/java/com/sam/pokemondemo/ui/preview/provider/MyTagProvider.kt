package com.sam.pokemondemo.ui.preview.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class MyTagProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String>
        get() = sequenceOf(
            "tag1",
            "tag2".repeat(5),
        )
}

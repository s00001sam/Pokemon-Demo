package com.sam.pokemondemo.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.sam.pokemondemo.ui.detail.BasicInfoSection
import com.sam.pokemondemo.ui.detail.EvolveSection
import com.sam.pokemondemo.ui.preview.model.BasicInfoSectionModel
import com.sam.pokemondemo.ui.preview.model.EvolveSectionModel
import com.sam.pokemondemo.ui.preview.provider.BasicInfoSectionModelProvider
import com.sam.pokemondemo.ui.preview.provider.EvolveSectionModelProvider
import com.sam.pokemondemo.ui.theme.PokemonDemoTheme

@OptIn(ExperimentalLayoutApi::class)
@Preview(
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
fun BasicInfoSectionPreview(
    @PreviewParameter(BasicInfoSectionModelProvider::class)
    model: BasicInfoSectionModel,
) {
    PokemonDemoTheme {
        BasicInfoSection(
            modifier = Modifier
                .fillMaxWidth(),
            pokemon = model.pokemon,
        )
    }
}

@Preview(
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
fun EvolveSectionPreview(
    @PreviewParameter(EvolveSectionModelProvider::class)
    model: EvolveSectionModel,
) {
    PokemonDemoTheme {
        EvolveSection(
            modifier = Modifier
                .fillMaxWidth(),
            evolvesFromId = model.evolvesFromId,
            evolvesFromName = model.evolvesFromName,
            evolvesFromImageUrl = model.evolvesFromImageUrl,
        )
    }
}

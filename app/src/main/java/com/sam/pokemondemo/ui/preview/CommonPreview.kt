package com.sam.pokemondemo.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.sam.pokemondemo.ui.MyImage
import com.sam.pokemondemo.ui.MyTag
import com.sam.pokemondemo.ui.preview.model.BasicInfoSectionModel
import com.sam.pokemondemo.ui.preview.provider.BasicInfoSectionModelProvider
import com.sam.pokemondemo.ui.preview.provider.MyTagProvider
import com.sam.pokemondemo.ui.theme.PokemonDemoTheme

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
fun MyImagePreview(
    @PreviewParameter(BasicInfoSectionModelProvider::class) model: BasicInfoSectionModel,
) {
    PokemonDemoTheme {
        MyImage(
            modifier = Modifier
                .size(100.dp),
            url = "",
            size = 100.dp,
            contentDescription = null,
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
fun MyTagPreview(
    @PreviewParameter(MyTagProvider::class) tagName: String,
) {
    PokemonDemoTheme {
        MyTag(
            tagName = tagName,
        )
    }
}

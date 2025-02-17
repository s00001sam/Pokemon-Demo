package com.sam.pokemondemo.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.sam.pokemondemo.ui.home.HomeEmptyView
import com.sam.pokemondemo.ui.home.HomePokemonItemView
import com.sam.pokemondemo.ui.home.TypeWithPokemonsItemView
import com.sam.pokemondemo.ui.preview.model.HomePokemonItemModel
import com.sam.pokemondemo.ui.preview.model.TypeWithPokemonsModel
import com.sam.pokemondemo.ui.preview.provider.HomePokemonItemModelProvider
import com.sam.pokemondemo.ui.preview.provider.TypeWithPokemonsModelProvider

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
fun TypeWithPokemonsItemViewPreview(
    @PreviewParameter(TypeWithPokemonsModelProvider::class)
    model: TypeWithPokemonsModel,
) {
    TypeWithPokemonsItemView(
        typeName = model.typeName,
        pokemons = model.pokemons,
        isBottomLineVisible = model.isBottomLineVisible,
    )
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
fun HomePokemonItemViewPreview(
    @PreviewParameter(HomePokemonItemModelProvider::class)
    model: HomePokemonItemModel,
) {
    HomePokemonItemView(
        pokemon = model.pokemon,
    )
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
fun HomeEmptyViewPreview() {
    HomeEmptyView()
}

package com.sam.pokemondemo.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sam.pokemondemo.Detail
import com.sam.pokemondemo.R
import com.sam.pokemondemo.model.BasicDisplayPokemon
import com.sam.pokemondemo.model.CapturedDisplayPokemon
import com.sam.pokemondemo.model.DisplayPokemon
import com.sam.pokemondemo.model.DisplayTypeWithPokemons
import com.sam.pokemondemo.ui.MyErrorSnackbar
import com.sam.pokemondemo.ui.MyImage
import com.sam.pokemondemo.ui.MyPullToRefreshBox
import com.sam.pokemondemo.ui.rememberLazyListState
import com.sam.pokemondemo.ui.theme.body
import com.sam.pokemondemo.ui.theme.headline1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val errorMessageRes by viewModel.errorMessageRes.collectAsStateWithLifecycle(null)
    val capturedPokemonList by viewModel.capturedPokemons.collectAsStateWithLifecycle(emptyList())
    val typeWithPokemonsList by viewModel.typeWithPokemons.collectAsStateWithLifecycle(emptyList())
    var isEmptyVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isLoading) {
        if (isLoading) isEmptyVisible = false
    }

    LaunchedEffect(errorMessageRes) {
        val res = errorMessageRes ?: return@LaunchedEffect
        snackBarHostState.showSnackbar(context.getString(res))
        if (typeWithPokemonsList.isEmpty()) {
            isEmptyVisible = true
        }
        viewModel.resetErrorMessage()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {},
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                MyErrorSnackbar(data)
            }
        }
    ) { contentPadding ->
        MyPullToRefreshBox(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            isRefreshing = isLoading,
            onRefresh = {
                viewModel.resetErrorMessage()
                viewModel.updatePokemonsFromRemote()
            },
        ) {
            HomeContent(
                modifier = Modifier
                    .fillMaxSize(),
                isEmptyVisible = isEmptyVisible,
                capturedPokemons = capturedPokemonList,
                typeWithPokemonsList = typeWithPokemonsList,
                onCapturedAdded = { pokemon ->
                    viewModel.addPokemonCaptured(
                        pokemonId = pokemon.pokemonId,
                    )
                },
                onCapturedRemoved = { pokemon ->
                    (pokemon as? CapturedDisplayPokemon)?.captureId?.let { captureId ->
                        viewModel.removePokemonCaptured(captureId)
                    }
                },
                toDetail = { pokemon ->
                    navController.navigate(route = Detail(pokemon.pokemonId))
                },
                onRetryClicked = {
                    viewModel.updatePokemonsFromRemote()
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    isEmptyVisible: Boolean,
    capturedPokemons: List<DisplayPokemon>,
    typeWithPokemonsList: List<DisplayTypeWithPokemons>,
    onCapturedAdded: (DisplayPokemon) -> Unit = {},
    onCapturedRemoved: (DisplayPokemon) -> Unit = {},
    toDetail: (DisplayPokemon) -> Unit = {},
    onRetryClicked: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        state = typeWithPokemonsList.rememberLazyListState()
    ) {
        // My Pocket
        stickyHeader {
            TypeWithPokemonsItemView(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth(),
                typeName = stringResource(R.string.my_pocket_title),
                pokemons = capturedPokemons,
                isBottomLineVisible = true,
                onPokemonClicked = toDetail,
                onCaptureClicked = onCapturedRemoved,
            )
        }

        // All Types and Pokemons
        items(
            count = typeWithPokemonsList.size,
            key = { i -> typeWithPokemonsList[i].type.name }
        ) { outsideIndex ->
            val typeWithPokemons = typeWithPokemonsList[outsideIndex]
            val type = typeWithPokemons.type
            val pokemons = typeWithPokemons.pokemons

            TypeWithPokemonsItemView(
                modifier = Modifier
                    .fillMaxWidth(),
                typeName = type.name,
                pokemons = pokemons,
                isBottomLineVisible = false,
                onPokemonClicked = toDetail,
                onCaptureClicked = onCapturedAdded,
            )
        }

        // Empty
        if (isEmptyVisible) item {
            EmptyView(
                modifier = Modifier
                    .fillMaxWidth(),
                onRetryClicked = onRetryClicked,
            )
        }
    }
}

@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    onRetryClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.empty_view_title),
            style = MaterialTheme.typography.headline1,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.size(24.dp))

        Button(
            onClick = {
                onRetryClicked()
            },
        ) {
            Text(
                text = stringResource(R.string.empty_view_button),
                style = MaterialTheme.typography.headline1,
                color = colorResource(R.color.button_text_color),
            )
        }
    }
}

@Composable
fun TypeWithPokemonsItemView(
    modifier: Modifier = Modifier,
    typeName: String,
    pokemons: List<DisplayPokemon>,
    isBottomLineVisible: Boolean = false,
    onPokemonClicked: (DisplayPokemon) -> Unit = {},
    onCaptureClicked: (DisplayPokemon) -> Unit = {},
) {
    val isEmpty = pokemons.isEmpty()

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = typeName,
                style = MaterialTheme.typography.headline1,
            )

            Spacer(
                modifier = Modifier
                    .size(16.dp),
            )

            Text(
                text = pokemons.size.toString(),
                style = MaterialTheme.typography.headline1,
            )
        }

        Spacer(Modifier.size(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (isEmpty) HomePokemonItemView(
                modifier = Modifier
                    .width(96.dp)
                    .alpha(0f),
                pokemon = BasicDisplayPokemon(name = ""),
                isClickable = false,
            )

            if (isEmpty) Text(
                text = stringResource(R.string.my_pocket_empty),
                style = MaterialTheme.typography.headline1,
            )

            if (!isEmpty) HomePokemonLazyRow(
                pokemons = pokemons,
                onPokemonClicked = onPokemonClicked,
                onCaptureClicked = onCaptureClicked,
            )
        }

        if (isBottomLineVisible) HorizontalDivider(
            modifier = Modifier
                .padding(top = 4.dp),
            thickness = 2.dp,
            color = Color.Blue,
        )
    }
}

@Composable
fun HomePokemonLazyRow(
    pokemons: List<DisplayPokemon>,
    onPokemonClicked: (DisplayPokemon) -> Unit = {},
    onCaptureClicked: (DisplayPokemon) -> Unit = {},
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        items(
            count = pokemons.size,
            key = { i ->
                val captureId = (pokemons[i] as? CapturedDisplayPokemon)?.captureId ?: -1
                "${pokemons[i].pokemonId}-$captureId"
            }
        ) { index ->
            Row {
                if (index == 0) Spacer(
                    modifier = Modifier.size(8.dp),
                )

                HomePokemonItemView(
                    modifier = Modifier
                        .width(96.dp),
                    pokemon = pokemons[index],
                    onPokemonClicked = onPokemonClicked,
                    onCaptureClicked = onCaptureClicked,
                )

                if (index == pokemons.size - 1) Spacer(
                    modifier = Modifier.size(8.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePokemonItemView(
    modifier: Modifier = Modifier,
    pokemon: DisplayPokemon,
    isClickable: Boolean = true,
    onPokemonClicked: (DisplayPokemon) -> Unit = {},
    onCaptureClicked: (DisplayPokemon) -> Unit = {},
) {
    Box(
        modifier = modifier
            .combinedClickable(
                enabled = isClickable,
                onClick = {
                    onPokemonClicked(pokemon)
                },
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MyImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                url = pokemon.imageUrl,
                contentDescription = pokemon.name,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = pokemon.name,
                style = MaterialTheme.typography.body,
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(36.dp)
                .clickable {
                    onCaptureClicked(pokemon)
                }
                .padding(4.dp),
            imageVector = Icons.Rounded.Favorite,
            tint = Color.Red,
            contentDescription = null,
        )
    }
}

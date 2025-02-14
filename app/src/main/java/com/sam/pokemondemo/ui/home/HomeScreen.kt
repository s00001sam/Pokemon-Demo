package com.sam.pokemondemo.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sam.pokemondemo.model.DisplayPokemon
import com.sam.pokemondemo.model.DisplayTypeWithPokemons
import com.sam.pokemondemo.ui.MyErrorSnackbar
import com.sam.pokemondemo.ui.MyImage
import com.sam.pokemondemo.ui.theme.body
import com.sam.pokemondemo.ui.theme.headline1

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val isLoading by viewModel.isLoading.collectAsState(false)
    val errorMessageRes by viewModel.errorMessageRes.collectAsState(null)

    LaunchedEffect(errorMessageRes) {
        val res = errorMessageRes ?: return@LaunchedEffect
        snackBarHostState.showSnackbar(context.getString(res))
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
        val typeWithPokemonsList by viewModel.typeWithPokemons.collectAsState(emptyList())

        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            HomeContent(
                modifier = Modifier
                    .fillMaxSize(),
                typeWithPokemonsList = typeWithPokemonsList,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    typeWithPokemonsList: List<DisplayTypeWithPokemons>,
) {
    LazyColumn(
        modifier = modifier,
    ) {
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
            )
        }
    }
}

@Composable
fun TypeWithPokemonsItemView(
    modifier: Modifier = Modifier,
    typeName: String,
    pokemons: List<DisplayPokemon>,
) {
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

        HomePokemonLazyRow(
            pokemons = pokemons,
        )
    }
}

@Composable
fun HomePokemonLazyRow(
    pokemons: List<DisplayPokemon>,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        items(
            count = pokemons.size,
        ) { index ->
            Row {
                if (index == 0) Spacer(
                    modifier = Modifier.size(8.dp),
                )

                HomePokemonItemView(
                    modifier = Modifier
                        .width(96.dp),
                    pokemon = pokemons[index],
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
                .size(40.dp)
                .clickable {
                    onCaptureClicked(pokemon)
                }
                .padding(8.dp),
            imageVector = Icons.Rounded.Favorite,
            tint = Color.Red,
            contentDescription = null,
        )
    }
}

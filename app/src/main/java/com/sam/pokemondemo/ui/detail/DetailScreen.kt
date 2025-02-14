package com.sam.pokemondemo.ui.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sam.pokemondemo.Detail
import com.sam.pokemondemo.R
import com.sam.pokemondemo.model.DetailDisplayPokemon
import com.sam.pokemondemo.ui.MyErrorSnackbar
import com.sam.pokemondemo.ui.MyImage
import com.sam.pokemondemo.ui.MyTag
import com.sam.pokemondemo.ui.theme.body
import com.sam.pokemondemo.ui.theme.headline1
import com.sam.pokemondemo.ui.theme.headline3
import com.sam.pokemondemo.ui.theme.title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessageRes by viewModel.errorMessageRes.collectAsState(null)

    LaunchedEffect(errorMessageRes) {
        val res = errorMessageRes ?: return@LaunchedEffect
        snackBarHostState.showSnackbar(context.getString(res))
        viewModel.resetErrorMessage()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                MyErrorSnackbar(data)
            }
        },
    ) { contentPadding ->
        val isLoading by viewModel.isLoading.collectAsState(false)
        val pokemon by viewModel.pokemon.collectAsStateWithLifecycle(null)

        PullToRefreshBox(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            isRefreshing = isLoading,
            onRefresh = {
                viewModel.refresh()
            },
        ) {
            DetailContent(
                modifier = Modifier
                    .fillMaxSize(),
                pokemon = pokemon,
                onEvolveClicked = { id ->
                    navController.navigate(Detail(id))
                },
            )

            DetailTopBar(
                modifier = Modifier.fillMaxWidth(),
                pokemonId = pokemon?.pokemonId,
                onBackClicked = {
                    navController.navigateUp()
                },
            )
        }
    }
}

@Composable
fun DetailTopBar(
    modifier: Modifier = Modifier,
    pokemonId: Int?,
    onBackClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onBackClicked()
            },
            content = {
                Icon(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(8.dp),
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    tint = colorResource(R.color.text_color),
                    contentDescription = stringResource(R.string.content_description_back),
                )
            }
        )

        if (pokemonId != null) Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = stringResource(
                R.string.pokemon_id,
                pokemonId,
            ),
            style = MaterialTheme.typography.headline1,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailContent(
    modifier: Modifier,
    pokemon: DetailDisplayPokemon?,
    onEvolveClicked: (id: Int) -> Unit = {},
) {
    pokemon ?: return

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.size(64.dp))

        BasicInfoSection(
            modifier = Modifier
                .fillMaxWidth(),
            pokemon = pokemon,
        )

        if (pokemon.evolvesFromId != -1) Spacer(
            modifier = Modifier.size(24.dp)
        )

        if (pokemon.evolvesFromId != -1) EvolveSection(
            modifier = Modifier
                .fillMaxWidth(),
            evolvesFromId = pokemon.evolvesFromId,
            evolvesFromName = pokemon.evolvesFromName,
            evolvesFromImageUrl = pokemon.evolvesFromImageUrl,
            onClicked = onEvolveClicked,
        )

        Spacer(Modifier.size(48.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = pokemon.description,
            style = MaterialTheme.typography.body,
        )
    }
}

@ExperimentalLayoutApi
@Composable
fun BasicInfoSection(
    modifier: Modifier = Modifier,
    pokemon: DetailDisplayPokemon,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MyImage(
            modifier = Modifier
                .size(160.dp),
            url = pokemon.imageUrl,
            contentDescription = pokemon.name,
        )

        Spacer(Modifier.size(32.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = pokemon.name,
            style = MaterialTheme.typography.title,
        )

        Spacer(Modifier.size(8.dp))

        if (pokemon.typeNames.isNotEmpty()) FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            pokemon.typeNames.forEach { typeName ->
                MyTag(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 8.dp),
                    tagName = typeName,
                )
            }
        }
    }
}

@Composable
fun EvolveSection(
    modifier: Modifier = Modifier,
    evolvesFromId: Int,
    evolvesFromName: String,
    evolvesFromImageUrl: String,
    onClicked: (id: Int) -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable {
                onClicked(evolvesFromId)
            }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.evolves_from_title),
                style = MaterialTheme.typography.headline3,
            )

            Spacer(Modifier.size(8.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = evolvesFromName,
                style = MaterialTheme.typography.body,
            )
        }

        Spacer(Modifier.size(8.dp))

        MyImage(
            modifier = Modifier
                .size(80.dp),
            url = evolvesFromImageUrl,
            contentDescription = evolvesFromName,
        )
    }
}

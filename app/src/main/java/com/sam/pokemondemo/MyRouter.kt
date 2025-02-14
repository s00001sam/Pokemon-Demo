package com.sam.pokemondemo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sam.pokemondemo.ui.detail.DetailScreen
import com.sam.pokemondemo.ui.detail.DetailViewModel
import com.sam.pokemondemo.ui.home.HomeScreen
import com.sam.pokemondemo.ui.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data class Detail(val pokemonId: Int)

@Composable
fun MyRouter(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home,
    ) {
        composable<Home> {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel<HomeViewModel>(),
            )
        }
        composable<Detail> {
            DetailScreen(
                navController = navController,
                viewModel = hiltViewModel<DetailViewModel>(),
            )
        }
    }
}

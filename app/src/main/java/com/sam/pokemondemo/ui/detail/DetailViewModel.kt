package com.sam.pokemondemo.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var pokemonId: Int? = null

    init {
        this.pokemonId = savedStateHandle.get<Int>(KEY_POKEMON_ID)
        Timber.d("sam00 pokemonId=$pokemonId")
    }

    companion object {
        const val KEY_POKEMON_ID = "pokemonId"
    }
}

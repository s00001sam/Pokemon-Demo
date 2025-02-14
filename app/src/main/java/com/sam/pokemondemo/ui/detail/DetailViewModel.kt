package com.sam.pokemondemo.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.pokemondemo.R
import com.sam.pokemondemo.model.DetailDisplayPokemon
import com.sam.pokemondemo.source.usecase.GetDetailPokemonUseCase
import com.sam.pokemondemo.source.usecase.UpdatePokemonDetailFromRemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val updatePokemonDetailFromRemote: UpdatePokemonDetailFromRemoteUseCase,
    private val getDetailPokemon: GetDetailPokemonUseCase,
) : ViewModel() {
    var pokemonId: Int? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessageRes = MutableStateFlow<Int?>(null)
    val errorMessageRes = _errorMessageRes.asStateFlow()

    private val _pokemon = MutableStateFlow<DetailDisplayPokemon?>(null)
    val pokemon = _pokemon.asStateFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    init {
        initId()?.let { id ->
            collectUpdatePokemonDetailState(id)
            collectPokemon(id)
        }
        refresh()
    }

    private fun initId(): Int? {
        this.pokemonId = savedStateHandle.get<Int>(KEY_POKEMON_ID)
        return pokemonId
    }

    private fun collectPokemon(pokemonId: Int? = this@DetailViewModel.pokemonId) {
        pokemonId ?: return

        viewModelScope.launch {
            getDetailPokemon.invoke(pokemonId).collectLatest {
                _pokemon.tryEmit(it)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectUpdatePokemonDetailState(id: Int) {
        viewModelScope.launch {
            refreshTrigger
                .flatMapLatest {
                    updatePokemonDetailFromRemote.invoke(id)
                }
                .collectLatest { state ->
                    when {
                        state.isLoading() -> {
                            _isLoading.tryEmit(true)
                        }

                        state.isError() -> {
                            Timber.e(state.getError()?.localizedMessage.toString())
                            _errorMessageRes.tryEmit(R.string.network_error_massage)
                            _isLoading.tryEmit(false)
                        }

                        else -> {
                            _isLoading.tryEmit(false)
                        }
                    }
                }
        }
    }

    fun refresh() {
        if (isLoading.value) return
        viewModelScope.launch(Dispatchers.IO) {
            refreshTrigger.emit(Unit)
        }
    }

    fun resetErrorMessage() {
        _errorMessageRes.tryEmit(null)
    }

    companion object {
        const val KEY_POKEMON_ID = "pokemonId"
    }
}

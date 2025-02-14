package com.sam.pokemondemo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.pokemondemo.R
import com.sam.pokemondemo.model.DisplayTypeWithPokemons
import com.sam.pokemondemo.source.usecase.GetTypeWithPokemonsUseCase
import com.sam.pokemondemo.source.usecase.UpdatePokemonsFromRemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val updatePokemonsFromRemote: UpdatePokemonsFromRemoteUseCase,
    private val getTypeWithPokemons: GetTypeWithPokemonsUseCase,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessageRes = MutableStateFlow<Int?>(null)
    val errorMessageRes = _errorMessageRes.asStateFlow()

    private val _typeWithPokemons = MutableStateFlow<List<DisplayTypeWithPokemons>>(emptyList())
    val typeWithPokemons = _typeWithPokemons.asStateFlow()

    init {
        collectUpdatePokemonsState()
        collectTypeWithPokemons()
    }

    private fun collectUpdatePokemonsState() {
        viewModelScope.launch {
            updatePokemonsFromRemote.invoke()
                .collectLatest { state ->
                    when {
                        state.isLoading() -> {
                            _isLoading.tryEmit(true)
                        }

                        state.isError() -> {
                            _isLoading.tryEmit(false)
                            _errorMessageRes.tryEmit(R.string.network_error_massage)
                        }

                        state.isSuccess() -> {
                            _isLoading.tryEmit(false)
                        }
                    }
                }
        }
    }

    private fun collectTypeWithPokemons() {
        viewModelScope.launch {
            getTypeWithPokemons.invoke().collectLatest {
                _typeWithPokemons.value = it
            }
        }
    }

}

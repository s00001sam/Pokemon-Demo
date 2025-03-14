package com.sam.pokemondemo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.pokemondemo.R
import com.sam.pokemondemo.model.DisplayPokemon
import com.sam.pokemondemo.model.DisplayTypeWithPokemons
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.repo.SharedPreferenceRepository
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import com.sam.pokemondemo.source.usecase.DeleteCaptureByIdUseCase
import com.sam.pokemondemo.source.usecase.GetCapturedPokemonsUseCase
import com.sam.pokemondemo.source.usecase.GetTypeWithPokemonsUseCase
import com.sam.pokemondemo.source.usecase.InsertCaptureUseCase
import com.sam.pokemondemo.source.usecase.UpdatePokemonsFromRemoteUseCase
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
class HomeViewModel @Inject constructor(
    private val spRepository: SharedPreferenceRepository,
    private val updatePokemonsFromRemote: UpdatePokemonsFromRemoteUseCase,
    private val getTypeWithPokemons: GetTypeWithPokemonsUseCase,
    private val getCapturedPokemons: GetCapturedPokemonsUseCase,
    private val insertCapture: InsertCaptureUseCase,
    private val deleteCaptureById: DeleteCaptureByIdUseCase,
    private val imagePreloader: ImagePreloader,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessageRes = MutableStateFlow<Int?>(null)
    val errorMessageRes = _errorMessageRes.asStateFlow()

    private val _typeWithPokemons = MutableStateFlow<List<DisplayTypeWithPokemons>>(emptyList())
    val typeWithPokemons = _typeWithPokemons.asStateFlow()

    private val _capturedPokemons = MutableStateFlow<List<DisplayPokemon>>(emptyList())
    val capturedPokemons = _capturedPokemons.asStateFlow()

    private val refreshTrigger = MutableSharedFlow<Boolean>()

    init {
        collectUpdatePokemonsState()
        collectTypeWithPokemons()
        collectCapturedPokemons()

        // load unfinished last time
        if (!spRepository.isLoadFinished) {
            updatePokemonsFromRemote(false)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectUpdatePokemonsState() {
        viewModelScope.launch {
            refreshTrigger
                .flatMapLatest {
                    updatePokemonsFromRemote.invoke(it)
                }
                .collectLatest { state ->
                    when {
                        state.isLoading() -> {
                            _isLoading.tryEmit(true)
                        }

                        state.isError() -> {
                            Timber.e(state.getError()?.localizedMessage.toString())
                            _isLoading.tryEmit(false)
                            _errorMessageRes.tryEmit(R.string.network_error_massage)
                        }

                        state.isSuccess() -> {
                            spRepository.isLoadFinished = true
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

    fun updatePokemonsFromRemote(isRefresh: Boolean) {
        if (isLoading.value) return
        spRepository.isLoadFinished = false
        viewModelScope.launch(Dispatchers.IO) {
            this@HomeViewModel.refreshTrigger.emit(isRefresh)
        }
    }

    private fun collectCapturedPokemons() {
        viewModelScope.launch {
            getCapturedPokemons.invoke().collectLatest {
                _capturedPokemons.value = it
            }
        }
    }

    fun removePokemonCaptured(captureId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteCaptureById.invoke(captureId)
        }
    }

    fun addPokemonCaptured(pokemonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val capture = CaptureEntity(
                pokemonId = pokemonId,
                capturedTime = System.currentTimeMillis(),
            )
            insertCapture.invoke(capture)
        }
    }

    fun resetErrorMessage() {
        _errorMessageRes.tryEmit(null)
    }

    fun preloadImages(imageUrls: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            imagePreloader.load(imageUrls)
        }
    }
}

package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.repo.FakeErrorRepository
import com.sam.pokemondemo.source.repo.FakeNormalRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UpdatePokemonDetailFromRemoteUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var normalRepo: FakeNormalRepository
    private lateinit var errorRepo: FakeErrorRepository
    private lateinit var normalUseCase: UpdatePokemonDetailFromRemoteUseCase
    private lateinit var errorUseCase: UpdatePokemonDetailFromRemoteUseCase

    @Before
    fun setup() {
        normalRepo = FakeNormalRepository()
        errorRepo = FakeErrorRepository()
        normalUseCase = UpdatePokemonDetailFromRemoteUseCase(normalRepo)
        errorUseCase = UpdatePokemonDetailFromRemoteUseCase(errorRepo)
        normalRepo.initAllBasicData()
        errorRepo.initAllBasicData()
    }

    /**
     * 模擬完全載入成功 (pokemonId = 5)
     * - 觸發 normalUseCase invoke()
     * - 檢查 應該有 loading state
     * - 檢查 資料庫 pokemon id 為 5 應該存在
     * - 檢查 資料庫 pokemon id 為 5 的 evolvesFromName 應該為空
     * - 檢查 應該有 success state
     * - 檢查 資料庫 pokemon id 為 5 的 evolvesFromName 應該為非空
     */
    @Test
    fun `confirm date update correctly`() = runTest {
        val currPokemonId = 5
        normalUseCase.invoke(currPokemonId).test {
            assertThat(awaitItem().isLoading()).isTrue()

            val prevPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(prevPokemon).isNotNull()

            assertThat(prevPokemon?.evolvesFromName.orEmpty()).isEmpty()

            assertThat(awaitItem().isSuccess()).isTrue()

            val nextPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(nextPokemon?.evolvesFromName.orEmpty()).isEqualTo("pokemon4")

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * 模擬完全載入失敗（遠端無此資料） (pokemonId = 30)
     * - 觸發 errorUseCase invoke()
     * - 檢查 應該有 loading state
     * - 檢查 資料庫 pokemon id 為 30 應該不存在
     * - 檢查 應該有 error state
     * - 檢查 資料庫 pokemon id 為 30 應該不存在
     */
    @Test
    fun `confirm data fetch error due to not found`() = runTest {
        val currPokemonId = 30
        normalUseCase.invoke(currPokemonId).test {
            assertThat(awaitItem().isLoading()).isTrue()

            val prevPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(prevPokemon).isNull()

            assertThat(awaitItem().isError()).isTrue()

            val nextPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(nextPokemon).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * 模擬完全載入失敗 (pokemonId = 5)
     * - 觸發 errorUseCase invoke()
     * - 檢查 應該有 loading state
     * - 檢查 資料庫 pokemon id 為 5 應該存在
     * - 檢查 資料庫 pokemon id 為 5 的 evolvesFromName 應該為空
     * - 檢查 應該有 error state
     * - 檢查 資料庫 pokemon id 為 5 的 evolvesFromName 應該為空
     */
    @Test
    fun `confirm data fetch error`() = runTest {
        val currPokemonId = 5
        errorUseCase.invoke(currPokemonId).test {
            assertThat(awaitItem().isLoading()).isTrue()
            val prevPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(prevPokemon).isNotNull()
            assertThat(prevPokemon?.evolvesFromName.orEmpty()).isEmpty()

            assertThat(awaitItem().isError()).isTrue()
            val nextPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(nextPokemon?.evolvesFromName.orEmpty()).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        normalRepo.clear()
        errorRepo.clear()
    }
}

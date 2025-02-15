package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.mockPokemons
import com.sam.pokemondemo.source.repo.FakeErrorRepository
import com.sam.pokemondemo.source.repo.FakeNormalRepository
import com.sam.pokemondemo.source.toPokemonEntities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdatePokemonsFromRemoteUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var normalRepo: FakeNormalRepository
    private lateinit var errorRepo: FakeErrorRepository
    private lateinit var normalUseCase: UpdatePokemonsFromRemoteUseCase
    private lateinit var errorUseCase: UpdatePokemonsFromRemoteUseCase

    @Before
    fun setup() {
        normalRepo = FakeNormalRepository()
        errorRepo = FakeErrorRepository()
        normalUseCase = UpdatePokemonsFromRemoteUseCase(normalRepo)
        errorUseCase = UpdatePokemonsFromRemoteUseCase(errorRepo)
    }

    /**
     * 模擬完全載入成功
     * - 觸發 normalUseCase invoke(true)
     * - 檢查 應該有 loading state
     * - 檢查 資料庫 pokemon 應該為空
     * - 檢查 資料庫 type 應該為空
     * - 檢查 應該有 success state
     * - 檢查 資料庫 pokemon 應該為 22 筆
     * - 檢查 資料庫 type 應該為 4 筆
     */
    @Test
    fun `confirm date update correctly`() = runTest {
        normalUseCase.invoke(true).test {
            assertThat(awaitItem().isLoading()).isTrue()
            assertThat(normalRepo.currPokemons.value.size).isEqualTo(0)
            assertThat(normalRepo.currTypes.value.size).isEqualTo(0)

            assertThat(awaitItem().isSuccess()).isTrue()
            assertThat(normalRepo.currPokemons.value.size).isEqualTo(22)
            assertThat(normalRepo.currTypes.value.size).isEqualTo(4)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * 模擬第一次未完全載入
     * - 先注入前五筆資料 pokemon1 ~ pokemon5
     * - 觸發 normalUseCase invoke(true)
     * - 檢查 資料庫本來應該是五筆
     * - 檢查 應該有 loading state
     * - 檢查 應該有 success state
     * - 檢查 從遠端拿到的資料不該包含 pokemon1
     * - 檢查 從遠端拿到的資料不該包含 pokemon5
     * - 檢查 從遠端拿到的資料應該包含 pokemon6
     */
    @Test
    fun `confirm subsequent first data fetch`() {
        normalRepo.currPokemons.tryEmit(
            mockPokemons.take(5).toPokemonEntities()
        )
        runTest {
            normalUseCase.invoke(false).test {
                assertThat(normalRepo.currPokemons.value.size).isEqualTo(5)
                assertThat(awaitItem().isLoading()).isTrue()
                assertThat(awaitItem().isSuccess()).isTrue()
                assertThat(normalRepo.pokemonsGotFromRemote.any { it == "pokemon1" }).isFalse()
                assertThat(normalRepo.pokemonsGotFromRemote.any { it == "pokemon5" }).isFalse()
                assertThat(normalRepo.pokemonsGotFromRemote.any { it == "pokemon6" }).isTrue()
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    /**
     * 模擬 API 錯誤
     * - 觸發 errorUseCase invoke(true)
     * - 檢查 資料庫 pokemon 應該空的
     * - 檢查 資料庫 type 本來應該空的
     * - 檢查 應該有 loading state
     * - 檢查 應該有 error state
     * - 檢查 資料庫 pokemon 應該空的
     * - 檢查 資料庫 type 應該空的
     */
    @Test
    fun `confirm data fetch error`() {
        runTest {
            errorUseCase.invoke(true).test {
                assertThat(awaitItem().isLoading()).isTrue()
                assertThat(normalRepo.currPokemons.value.size).isEqualTo(0)
                assertThat(normalRepo.currTypes.value.size).isEqualTo(0)

                assertThat(awaitItem().isError()).isTrue()
                assertThat(normalRepo.currPokemons.value.size).isEqualTo(0)
                assertThat(normalRepo.currTypes.value.size).isEqualTo(0)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @After
    fun clear() {
        normalRepo.clear()
        errorRepo.clear()
    }
}

package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.repo.FakeNormalRepository
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCapturedPokemonsUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: FakeNormalRepository
    private lateinit var useCase: GetCapturedPokemonsUseCase

    @Before
    fun setup() {
        repo = FakeNormalRepository().apply { initAllBasicData() }
        useCase = GetCapturedPokemonsUseCase(repo)
    }

    /**
     * 測試捕捉列表收集
     * - 觸發 useCase invoke()
     * - 確認 資料庫至少有一隻 Pokemon 可以抓
     * - 檢查 初始化的捕捉數量應該為 0
     * - 捕捉資料庫的第一隻 Pokemon
     * - 檢查 目前的捕捉數量應該為 1
     */
    @Test
    fun `confirm capture list collect is correct`() = runTest {
        useCase.invoke().test {
            assertThat(repo.currPokemons.value.size).isGreaterThan(1)

            assertThat(awaitItem().size).isEqualTo(0)

            repo.insertCapture(
                CaptureEntity(
                    id = 1,
                    pokemonId = repo.currPokemons.value[0].id,
                    capturedTime = System.currentTimeMillis(),
                ),
            )
            assertThat(awaitItem().size).isEqualTo(1)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * 測試可以重複收集同一隻 Pokemon
     * - 觸發 useCase invoke()
     * - 確認 資料庫至少有一隻 Pokemon 可以抓
     * - 檢查 初始化的捕捉數量應該為 0
     * - 捕捉資料庫的第一隻 Pokemon
     * - 捕捉資料庫的第一隻 Pokemon
     * - 檢查 目前的捕捉數量應該為 2
     * - 檢查 捕捉列表裡面第一筆和第二筆 pokemonId 應該一樣
     */
    @Test
    fun `confirm can capture the same pokemon`() = runTest {
        useCase.invoke().test {
            assertThat(repo.currPokemons.value.size).isGreaterThan(1)

            assertThat(awaitItem().size).isEqualTo(0)

            repo.insertCapture(
                CaptureEntity(
                    id = 1,
                    pokemonId = repo.currPokemons.value[0].id,
                    capturedTime = System.currentTimeMillis(),
                ),
            )
            awaitItem()

            repo.insertCapture(
                CaptureEntity(
                    id = 2,
                    pokemonId = repo.currPokemons.value[0].id,
                    capturedTime = System.currentTimeMillis(),
                ),
            )
            val result = awaitItem()
            assertThat(result.size).isEqualTo(2)
            assertThat(result[0].pokemonId).isEqualTo(result[1].pokemonId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * 測試捕捉列表收集會依照捕捉時間排序(DESC)
     * - 觸發 useCase invoke()
     * - 確認 資料庫至少有一隻 Pokemon 可以抓
     * - 檢查 初始化的捕捉數量應該為 0
     * - 捕捉資料庫的第一隻 Pokemon
     * - 延遲 1000ms
     * - 捕捉資料庫的第一隻 Pokemon（因為時間可能拿到一樣所以補加上 1000ms）
     * - 檢查 目前的捕捉數量應該為 2
     * - 檢查 捕捉列表裡面第一筆和第二筆 pokemonId 應該一樣
     */
    @Test
    fun `confirm capture list sort by captureTime DESC`() = runTest {
        useCase.invoke().test {
            assertThat(repo.currPokemons.value.size).isGreaterThan(1)

            assertThat(awaitItem().size).isEqualTo(0)

            repo.insertCapture(
                CaptureEntity(
                    id = 1,
                    pokemonId = repo.currPokemons.value[0].id,
                    capturedTime = System.currentTimeMillis(),
                ),
            )
            awaitItem()

            repo.insertCapture(
                CaptureEntity(
                    id = 2,
                    pokemonId = repo.currPokemons.value[0].id,
                    capturedTime = System.currentTimeMillis() + 1000L,
                ),
            )
            val result = awaitItem()
            println(result)
            assertThat(result.size).isEqualTo(2)
            assertThat(result[0].capturedTime).isGreaterThan(result[1].capturedTime)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        repo.clear()
    }
}

package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.model.CapturedDisplayPokemon
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
     * Testing the capture list
     * - trigger useCase invoke()
     * - Confirmed: database has at least one catchable Pokemon
     * - Confirmed: initial capture count should be 0
     * - capture 1st Pokemon in the database
     * - Confirmed: capture count should be 1
     * - Confirmed: return type in list should be CapturedDisplayPokemon
     */
    @Test
    fun `confirmed capture list is correct`() = runTest {
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
            val result = awaitItem()
            assertThat(result.size).isEqualTo(1)
            assertThat(result[0]).isInstanceOf(CapturedDisplayPokemon::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Test capturing duplicate Pokemon
     * - trigger useCase invoke()
     * - Confirmed: database has at least one catchable Pokemon
     * - Confirmed: initial capture count should be 0
     * - capture 1st Pokemon in the database
     * - capture 1st Pokemon in the database again
     * - Confirmed: capture count should be 2
     * - Confirmed: 1st and 2nd captures have the same id
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
     * Test capture list is sorted by capture time (descending)
     * - trigger useCase invoke()
     * - Confirmed: database has at least one catchable Pokemon
     * - Confirmed: initial capture count should be 0
     * - capture 1st Pokemon in the database
     * - capture 1st Pokemon in the database again（due to the duplicate timestamps, a 1000ms offset is included）
     * - Confirmed: capture count should be 2
     * - Confirmed: 1st captureTime should be greater than the 2nd
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

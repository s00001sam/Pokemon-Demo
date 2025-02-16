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
class InsertCaptureUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: FakeNormalRepository
    private lateinit var useCase: InsertCaptureUseCase

    @Before
    fun setup() {
        repo = FakeNormalRepository().apply { initAllBasicData() }
        useCase = InsertCaptureUseCase(repo)
    }

    /**
     * Test capture a pokemon
     * - Confirmed: no initial capture
     * - trigger useCase invoke() to capture 1st Pokemon in the database
     * - Confirmed: size of capture list should be 1
     * - Confirmed: id of 1st capture should be 1
     */
    @Test
    fun `insert capture success`() = runTest {
        repo.currCaptures.test {
            assertThat(awaitItem().size).isEqualTo(0)

            val currPokemons = repo.currPokemons.value
            val capture = CaptureEntity(
                id = 1,
                pokemonId = currPokemons[0].id,
                capturedTime = System.currentTimeMillis(),
            )
            useCase.invoke(capture)
            val result = awaitItem()
            assertThat(result.size).isEqualTo(1)
            assertThat(result[0].pokemonId).isEqualTo(1)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Test capturing duplicate Pokemon
     * - Confirmed: capture list should be empty
     * - trigger useCase invoke() to capture 1st Pokemon in the database
     * - trigger useCase invoke() to capture 1st Pokemon in the database again
     * - Confirmed: size of capture list should be 2
     * - Confirmed: 1st and 2nd captures should have the same id
     */
    @Test
    fun `insert capture duplicate Pokemon`() = runTest {
        repo.currCaptures.test {
            assertThat(awaitItem()).isEmpty()

            val pokemons = repo.currPokemons.value
            val capture1 = CaptureEntity(
                id = 1,
                pokemonId = pokemons[0].id,
                capturedTime = System.currentTimeMillis(),
            )
            useCase.invoke(capture1)
            awaitItem()

            val capture2 = CaptureEntity(
                id = 2,
                pokemonId = pokemons[0].id,
                capturedTime = System.currentTimeMillis(),
            )
            useCase.invoke(capture2)
            val result2 = awaitItem()
            assertThat(result2.size).isEqualTo(2)
            assertThat(result2[0].pokemonId).isEqualTo(result2[1].pokemonId)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        repo.clear()
    }
}

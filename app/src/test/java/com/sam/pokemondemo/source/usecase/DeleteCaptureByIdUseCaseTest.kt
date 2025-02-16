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
class DeleteCaptureByIdUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: FakeNormalRepository
    private lateinit var useCase: DeleteCaptureByIdUseCase

    @Before
    fun setup() {
        repo = FakeNormalRepository().apply { initAllBasicData() }
        useCase = DeleteCaptureByIdUseCase(repo)
    }

    /**
     * Test deletion a capture
     * - capture 1st Pokemon in the pokemon database
     * - Confirmed: size of capture list should be 1
     * - trigger useCase invoke(1) to delete capture with id 1 from the capture database
     * - Confirmed: capture list should be empty
     */
    @Test
    fun `test deletion a capture successful`() = runTest {
        val currPokemons = repo.currPokemons.value
        val capture = CaptureEntity(
            id = 1,
            pokemonId = currPokemons[0].id,
            capturedTime = System.currentTimeMillis(),
        )
        repo.insertCapture(capture)
        repo.currCaptures.test {
            assertThat(awaitItem().size).isEqualTo(1)

            useCase.invoke(1)
            assertThat(awaitItem()).isEmpty()

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Test delete a capture that doesn't exist
     * - capture 1st Pokemon in the pokemon database
     * - Confirmed: size of capture list should be 1
     * - trigger useCase invoke(2) to delete capture with id 2 from the capture database
     * - Confirmed: size of capture list should be 1
     */
    @Test
    fun `test delete a capture that doesn't exist`() = runTest {
        val currPokemons = repo.currPokemons.value
        val capture = CaptureEntity(
            id = 1,
            pokemonId = currPokemons[0].id,
            capturedTime = System.currentTimeMillis(),
        )
        repo.insertCapture(capture)

        assertThat(repo.currCaptures.value.size).isEqualTo(1)

        useCase.invoke(2)
        assertThat(repo.currCaptures.value.size).isEqualTo(1)
    }

    @After
    fun tearDown() {
        repo.clear()
    }
}

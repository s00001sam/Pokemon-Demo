package com.sam.pokemondemo.source.usecase

import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetCapturedPokemonsUseCaseTest {
    private lateinit var repo: BaseRepository
    private lateinit var useCase: GetCapturedPokemonsUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        useCase = GetCapturedPokemonsUseCase(repo)
    }

    /**
     * Test call method in repo
     * - mock repo.getLocalCapturedPokemonsByTimeDesc() output
     * - trigger useCase invoke()
     * - Confirmed: check if calls method getLocalCapturedPokemonsByTimeDesc in repo
     * - Confirmed: the output data (size)
     * - Confirmed: the output data (captureId)
     * - Confirmed: the output data (pokemonId)
     * - Confirmed: the output data (name)
     */
    @Test
    fun `test call method getLocalCapturedPokemonsByTimeDesc in repo`() = runTest {
        val capturedPokemons = listOf(
            CapturedPokemonView(
                id = 0,
                pokemonId = 1,
                name = "pokemon1",
                capturedTime = System.currentTimeMillis(),
            ),
        )
        every { repo.getLocalCapturedPokemonsByTimeDesc() } returns flowOf(capturedPokemons)

        val result = useCase.invoke().first()

        coVerify { repo.getLocalCapturedPokemonsByTimeDesc() }
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].captureId).isEqualTo(capturedPokemons[0].id)
        assertThat(result[0].pokemonId).isEqualTo(capturedPokemons[0].pokemonId)
        assertThat(result[0].name).isEqualTo(capturedPokemons[0].name)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

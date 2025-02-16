package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.repo.FakeErrorRepository
import com.sam.pokemondemo.source.repo.FakeNormalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdatePokemonDetailFromRemoteUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var normalRepo: FakeNormalRepository
    private lateinit var errorRepo: FakeErrorRepository
    private lateinit var normalUseCase: UpdatePokemonDetailFromRemoteUseCase
    private lateinit var errorUseCase: UpdatePokemonDetailFromRemoteUseCase

    @Before
    fun setup() {
        normalRepo = FakeNormalRepository().apply { initAllBasicData() }
        errorRepo = FakeErrorRepository().apply { initAllBasicData() }
        normalUseCase = UpdatePokemonDetailFromRemoteUseCase(normalRepo)
        errorUseCase = UpdatePokemonDetailFromRemoteUseCase(errorRepo)
    }

    /**
     * Test load successful (pokemonId = 5)
     * - trigger normalUseCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: Pokemon with id 5 should be in the database
     * - Confirmed: evolvesFromName of pokemon with id 5 is empty
     * - Confirmed: status should be success
     * - Confirmed: evolvesFromName of pokemon with id 5 is not empty
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
     * Test load failure (data not found remotely) (pokemonId = 30)
     * - trigger errorUseCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: pokemon id 30 should not exist in the database
     * - Confirmed: status should be error
     * - Confirmed: pokemon id 30 should not exist in the database
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
     * Test load failure (pokemonId = 5)
     * - trigger errorUseCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: pokemon id 5 should not exist in the database
     * - Confirmed: evolvesFromName of pokemon with id 5 is empty
     * - Confirmed: status should be error
     * - Confirmed: evolvesFromName of pokemon with id 5 is empty
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

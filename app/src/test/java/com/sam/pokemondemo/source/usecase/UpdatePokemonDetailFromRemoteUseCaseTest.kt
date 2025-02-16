package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.imagepreloader.FakeImagePreloader
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
    private lateinit var imagePreloader: FakeImagePreloader
    private lateinit var normalUseCase: UpdatePokemonDetailFromRemoteUseCase
    private lateinit var errorUseCase: UpdatePokemonDetailFromRemoteUseCase

    @Before
    fun setup() {
        normalRepo = FakeNormalRepository().apply { initAllBasicData() }
        errorRepo = FakeErrorRepository().apply { initAllBasicData() }
        imagePreloader = FakeImagePreloader()
        normalUseCase = UpdatePokemonDetailFromRemoteUseCase(normalRepo, imagePreloader)
        errorUseCase = UpdatePokemonDetailFromRemoteUseCase(errorRepo, imagePreloader)
    }

    /**
     * Test load successful (pokemonId = 6)
     * - trigger normalUseCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: Pokemon with id 6 should be in the database
     * - Confirmed: evolvesFromName of pokemon with id 6 should be empty
     * - Confirmed: status should be success
     * - Confirmed: evolvesFromName of pokemon with id 6 should be not empty
     */
    @Test
    fun `test load successful`() = runTest {
        val currPokemonId = 6
        normalUseCase.invoke(currPokemonId).test {
            assertThat(awaitItem().isLoading()).isTrue()

            val prevPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(prevPokemon).isNotNull()

            assertThat(prevPokemon?.evolvesFromName.orEmpty()).isEmpty()

            assertThat(awaitItem().isSuccess()).isTrue()

            val nextPokemon = normalRepo.currPokemons.value.find { it.id == currPokemonId }
            assertThat(nextPokemon?.evolvesFromName.orEmpty()).isEqualTo("pokemon5")

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Test load image successful (pokemonId = 6)
     * - Confirmed: downloaded images should be empty
     * - trigger normalUseCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: status should be success
     * - Confirmed: downloaded image should be "https://com.sam.pokemon/image6"
     */
    @Test
    fun `test load image successful`() = runTest {
        val currPokemonId = 6

        assertThat(imagePreloader.downloadImages).isEmpty()

        normalUseCase.invoke(currPokemonId).test {
            assertThat(awaitItem().isLoading()).isTrue()

            assertThat(awaitItem().isSuccess()).isTrue()

            assertThat(imagePreloader.downloadImages[0]).isEqualTo("https://com.sam.pokemon/image6")

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
    fun `test load failure with data not found`() = runTest {
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
     * Test load failure (pokemonId = 6)
     * - trigger errorUseCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: pokemon id 6 should not exist in the database
     * - Confirmed: evolvesFromName of pokemon with id 6 should be empty
     * - Confirmed: status should be error
     * - Confirmed: evolvesFromName of pokemon with id 6 should be empty
     */
    @Test
    fun `test load failure with error`() = runTest {
        val currPokemonId = 6
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
        imagePreloader.clear()
    }
}

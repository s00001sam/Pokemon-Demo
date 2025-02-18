package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.imagepreloader.FakeImagePreloader
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
    private lateinit var imagePreloader: FakeImagePreloader
    private lateinit var normalUseCase: UpdatePokemonsFromRemoteUseCase
    private lateinit var errorUseCase: UpdatePokemonsFromRemoteUseCase

    @Before
    fun setup() {
        normalRepo = FakeNormalRepository()
        errorRepo = FakeErrorRepository()
        imagePreloader = FakeImagePreloader()
        normalUseCase = UpdatePokemonsFromRemoteUseCase(normalRepo, imagePreloader)
        errorUseCase = UpdatePokemonsFromRemoteUseCase(errorRepo, imagePreloader)
    }

    /**
     * Test load successful
     * - trigger normalUseCase invoke(true)
     * - Confirmed: status should be loading
     * - Confirmed: pokemon database should be empty
     * - Confirmed: type database should be empty
     * - Confirmed: status should be success
     * - Confirmed: size of pokemon database should be 22
     * - Confirmed: size of type database should be 4
     */
    @Test
    fun `test load successful`() = runTest {
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
     * Test load images successful
     * - Confirmed: downloaded images should be empty
     * - trigger normalUseCase invoke(true)
     * - Confirmed: status should be loading
     * - Confirmed: status should be success
     * - Confirmed: size of downloaded images should be 22
     */
    @Test
    fun `test load images successful`() = runTest {
        assertThat(imagePreloader.downloadImages).isEmpty()

        normalUseCase.invoke(true).test {
            assertThat(awaitItem().isLoading()).isTrue()

            assertThat(awaitItem().isSuccess()).isTrue()

            assertThat(imagePreloader.downloadImages.size).isEqualTo(22)

            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Test load failed to fully complete on the first attempt
     * - inject the first five entries (pokemon1 ~ pokemon5)
     * - trigger normalUseCase invoke(true)
     * - Confirmed: size of pokemon database should be 5
     * - Confirmed: status should be loading
     * - Confirmed: status should be success
     * - Confirmed: data retrieved from remote should not include pokemon1
     * - Confirmed: data retrieved from remote should not include pokemon5
     * - Confirmed: data retrieved from remote should include pokemon6
     */
    @Test
    fun `Test load failed to fully complete on the first attempt`() {
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
     * Test load failure with error
     * - trigger errorUseCase invoke(true)
     * - Confirmed: status should be loading
     * - Confirmed: pokemon database should be empty
     * - Confirmed: type database should be empty
     * - Confirmed: status should be error
     * - Confirmed: pokemon database should be empty
     * - Confirmed: type database should be empty
     */
    @Test
    fun `test load failure with error`() {
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
    fun tearDown() {
        normalRepo.clear()
        errorRepo.clear()
        imagePreloader.clearFakeData()
    }
}

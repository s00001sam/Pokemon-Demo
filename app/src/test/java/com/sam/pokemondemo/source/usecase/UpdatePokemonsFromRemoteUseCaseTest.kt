package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.apiservice.PokemonApiService.Companion.DEFAULT_GET_POKEMONS_URL
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.mockBasicPokemonsResponse
import com.sam.pokemondemo.source.mockRemotePokemonResponses
import com.sam.pokemondemo.source.mockUrlToPokemonMap
import com.sam.pokemondemo.source.repo.BaseRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class UpdatePokemonsFromRemoteUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: BaseRepository
    private lateinit var imagePreloader: ImagePreloader
    private lateinit var useCase: UpdatePokemonsFromRemoteUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        imagePreloader = mockk<ImagePreloader>(relaxed = true)
        useCase = UpdatePokemonsFromRemoteUseCase(repo, imagePreloader)
    }

    /**
     * Test load successful
     * - mock methods
     * - trigger useCase invoke(true)
     * - Confirmed: status should be loading
     * - Confirmed: status should be success
     * - Verify: repo.getRemoteBasicPokemons() should trigger exactly 1 time
     * - Verify: repo.clearLocalDataWithoutCapture() should trigger exactly 1 time
     * - Verify: repo.getRemotePokemon(Url) should trigger exactly 12 times
     * - Confirmed: saved images size should be 12
     * - Confirmed: the last saved image should be correct
     */
    @Test
    fun test_load_successful_when_refresh() = runTest {
        coEvery { repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL) } returns Response.success(
            mockBasicPokemonsResponse
        )
        coEvery { repo.clearLocalDataWithoutCapture() } just runs
        coEvery { imagePreloader.clear() } just runs
        coEvery { repo.getLocalPokemonNames() } returns emptyList()
        mockUrlToPokemonMap.forEach { (url, pokemon) ->
            coEvery { repo.getRemotePokemon(url) } returns Response.success(pokemon)
        }
        val captureImages = mutableListOf<List<String>>()
        coEvery { imagePreloader.load(capture(captureImages)) } just runs
        coEvery { repo.upsertBasicPokemonsAndTypes(any(), any(), any()) } just runs

        useCase.invoke(true).test {
            assertThat(awaitItem().isLoading()).isTrue()
            assertThat(awaitItem().isSuccess()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL) }
        coVerify(exactly = 1) { repo.clearLocalDataWithoutCapture() }
        coVerify(exactly = 12) { repo.getRemotePokemon(any() as String) }

        val allCaptureImages = captureImages[0] + captureImages[1]
        assertThat(allCaptureImages.size).isEqualTo(12)

        val expectedLastImage = mockRemotePokemonResponses.last()
            .sprites?.other?.officialArtwork?.frontDefault
        assertThat(captureImages[1].last()).isEqualTo(expectedLastImage)
    }

    /**
     * Test load failed to fully complete on the first attempt
     * - mock methods (There are already five pokemons in DB)
     * - trigger useCase invoke(true)
     * - Confirmed: status should be loading
     * - Confirmed: status should be success
     * - Verify: repo.getRemoteBasicPokemons() should trigger exactly 1 time
     * - Verify: repo.clearLocalDataWithoutCapture() should not trigger
     * - Verify: repo.getRemotePokemon(Url) should trigger exactly (12 - 5) times
     * - Confirmed: saved images size should be (12 - 5) this time
     */
    @Test
    fun test_load_failed_to_fully_complete_on_the_first_attempt() = runTest {
        val prevSavedPokemonNames = mockRemotePokemonResponses
            .take(5)
            .mapNotNull { it.name }

        coEvery { repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL) } returns Response.success(
            mockBasicPokemonsResponse
        )
        coEvery { repo.clearLocalDataWithoutCapture() } just runs
        coEvery { imagePreloader.clear() } just runs
        coEvery { repo.getLocalPokemonNames() } returns prevSavedPokemonNames
        mockUrlToPokemonMap.forEach { (url, pokemon) ->
            coEvery { repo.getRemotePokemon(url) } returns Response.success(pokemon)
        }
        val captureImages = mutableListOf<List<String>>()
        coEvery { imagePreloader.load(capture(captureImages)) } just runs
        coEvery { repo.upsertBasicPokemonsAndTypes(any(), any(), any()) } just runs

        useCase.invoke(false).test {
            assertThat(awaitItem().isLoading()).isTrue()
            assertThat(awaitItem().isSuccess()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL) }
        coVerify(exactly = 0) { repo.clearLocalDataWithoutCapture() }
        coVerify(exactly = (12 - 5)) { repo.getRemotePokemon(any() as String) }

        val allCaptureImages = captureImages[0] + captureImages[1]
        assertThat(allCaptureImages.size).isEqualTo((12 - 5))
    }

    /**
     * Test load failure with error
     * - trigger errorUseCase invoke(true)
     * - Confirmed: status should be loading
     * - Confirmed: status should be error
     * - Verify: repo.clearLocalDataWithoutCapture() should not trigger
     * - Verify: repo.getRemotePokemon(Url) should not trigger
     * - Verify: imagePreloader.load(List<String>) should not trigger
     */
    @Test
    fun test_load_failure_with_error() = runTest {
        val errorResponseBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { repo.getRemoteBasicPokemons(DEFAULT_GET_POKEMONS_URL) } returns Response.error(
            404,
            errorResponseBody,
        )

        useCase.invoke(true).test {
            assertThat(awaitItem().isLoading()).isTrue()
            assertThat(awaitItem().isError()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { repo.clearLocalDataWithoutCapture() }
        coVerify(exactly = 0) { repo.getRemotePokemon(any() as String) }
        coVerify(exactly = 0) { imagePreloader.load(any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

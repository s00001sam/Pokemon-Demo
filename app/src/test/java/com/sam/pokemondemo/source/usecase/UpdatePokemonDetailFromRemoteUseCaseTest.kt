package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.mockPokemon1Image
import com.sam.pokemondemo.source.mockPokemon1Name
import com.sam.pokemondemo.source.mockPokemon1SpeciesResponseForDetail
import com.sam.pokemondemo.source.mockRemotePokemonResponseForDetail
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
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
class UpdatePokemonDetailFromRemoteUseCaseTest {
    @get:Rule
    private val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: BaseRepository

    private lateinit var imagePreloader: ImagePreloader

    private lateinit var useCase: UpdatePokemonDetailFromRemoteUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        imagePreloader = mockk<ImagePreloader>(relaxed = true)
        useCase = UpdatePokemonDetailFromRemoteUseCase(
            repo = repo,
            imagePreloader = imagePreloader,
        )
    }

    /**
     * Test load successful (pokemonId = 1)
     * - mock methods
     * - trigger useCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: status should be success
     * - Verify: repo.getRemotePokemon() should trigger exactly 1 time
     * - Verify: repo.getRemotePokemonSpecies() should trigger exactly 1 time
     * - Confirmed: saved images should be correct
     * - Confirmed: saved pokemon name should be correct
     */
    @Test
    fun test_load_successful() = runTest {
        val id = 1
        coEvery { repo.getRemotePokemon(id) } returns Response.success(
            mockRemotePokemonResponseForDetail
        )
        coEvery { repo.getRemotePokemonSpecies(id) } returns Response.success(
            mockPokemon1SpeciesResponseForDetail
        )
        val captureImages = slot<List<String>>()
        coEvery { imagePreloader.load(capture(captureImages)) } just runs
        val capturePokemonEntity = slot<PokemonEntity>()
        coEvery { repo.updateDetails(capture(capturePokemonEntity), any(), any()) } just runs

        useCase.invoke(id).test {
            assertThat(awaitItem().isLoading()).isTrue()
            assertThat(awaitItem().isSuccess()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { repo.getRemotePokemon(id) }
        coVerify(exactly = 1) { repo.getRemotePokemonSpecies(id) }
        assertThat(captureImages.captured).isEqualTo(listOf(mockPokemon1Image))
        assertThat(capturePokemonEntity.captured.name).isEqualTo(mockPokemon1Name)
    }

    /**
     * Test load failure (data not found remotely) (pokemonId = 1)
     * - mock methods
     * - trigger useCase invoke()
     * - Confirmed: status should be loading
     * - Confirmed: status should be Error
     * - Verify: imagePreloader.load should not trigger
     * - Verify: repo.updateDetails should not trigger
     */
    @Test
    fun test_load_failure_with_error() = runTest {
        val id = 1
        val errorResponseBody = "".toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { repo.getRemotePokemon(id) } returns Response.error(404, errorResponseBody)
        coEvery { repo.getRemotePokemonSpecies(id) } returns Response.error(404, errorResponseBody)
        coEvery { imagePreloader.load(any()) } just runs
        coEvery { repo.updateDetails(any(), any(), any()) } just runs

        useCase.invoke(id).test {
            assertThat(awaitItem().isLoading()).isTrue()
            assertThat(awaitItem().isError()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { imagePreloader.load(any()) }
        coVerify(exactly = 0) { repo.updateDetails(any(), any(), any()) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

package com.sam.pokemondemo.source.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.TestCoroutineRule
import com.sam.pokemondemo.source.repo.FakeNormalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetDetailPokemonUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: FakeNormalRepository
    private lateinit var useCase: GetDetailPokemonUseCase

    @Before
    fun setup() {
        repo = FakeNormalRepository().apply { initAllBasicData() }
        useCase = GetDetailPokemonUseCase(repo)
    }

    /**
     * Test get details of pokemon
     * - trigger useCase invoke(2) to get pokemon2 details
     * - Confirmed: name of pokemon1 in the database should not be empty
     * - Confirmed: evolvesFromName of pokemon2 in the database should be empty
     * - initialize details for pokemon2
     * - Confirmed: evolvesFromName of pokemon2 in the database should be not empty
     */
    @Test
    fun `test get details of pokemon`() = runTest {
        useCase.invoke(2).test {
            val result1 = awaitItem()
            assertThat(result1.name).isNotEmpty()
            assertThat(result1.evolvesFromName).isEmpty()

            repo.initDetailsData(2)
            val result2 = awaitItem()
            assertThat(result2.evolvesFromName).isNotEmpty()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        repo.clear()
    }
}

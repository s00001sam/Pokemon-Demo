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
class GetTypeWithPokemonsUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repo: FakeNormalRepository
    private lateinit var useCase: GetTypeWithPokemonsUseCase

    @Before
    fun setup() {
        repo = FakeNormalRepository()
        useCase = GetTypeWithPokemonsUseCase(repo)
    }

    /**
     * Test get typeWithPokemons
     * - trigger useCase invoke() to get typeWithPokemons list
     * - Confirmed: typeWithPokemons list should be empty
     * - insert all basic data to the database
     * - Confirmed: size of typeWithPokemons list should be 4
     */
    @Test
    fun `test get details of pokemon`() = runTest {
        useCase.invoke().test {
            val result1 = awaitItem()
            assertThat(result1.size).isEqualTo(0)

            repo.initAllBasicData()
            awaitItem()
            awaitItem()

            val result2 = awaitItem()
            assertThat(result2.size).isEqualTo(4)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        repo.clear()
    }
}

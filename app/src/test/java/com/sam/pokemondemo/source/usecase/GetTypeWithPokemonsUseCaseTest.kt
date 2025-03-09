package com.sam.pokemondemo.source.usecase

import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons
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

class GetTypeWithPokemonsUseCaseTest {
    private lateinit var repo: BaseRepository
    private lateinit var useCase: GetTypeWithPokemonsUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        useCase = GetTypeWithPokemonsUseCase(repo)
    }

    /**
     * Test call method in repo
     * - mock repo.getLocalTypeWithPokemons() output
     * - trigger useCase invoke()
     * - Confirmed: check if calls method getLocalTypeWithPokemons in repo
     * - Confirmed: the output data (size)
     * - Confirmed: the output data (first typeName)
     * - Confirmed: the output data (pokemons.size)
     * - Confirmed: the output data (first pokemon name)
     */
    @Test
    fun `test call method getLocalTypeWithPokemons in repo`() = runTest {
        val typeWithPokemons = listOf(
            TypeWithPokemons(
                type = TypeEntity("type1"),
                pokemons = listOf(
                    PokemonEntity(
                        id = 1,
                        name = "pokemon1",
                        description = "description1",
                    ),
                    PokemonEntity(
                        id = 2,
                        name = "pokemon2",
                        description = "description2",
                    ),
                )
            )
        )
        every { repo.getLocalTypeWithPokemons() } returns flowOf(typeWithPokemons)

        val result = useCase.invoke().first()

        coVerify { repo.getLocalTypeWithPokemons() }
        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].type.name).isEqualTo(typeWithPokemons[0].type.name)
        assertThat(result[0].pokemons.size).isEqualTo(2)
        assertThat(result[0].pokemons[0].name).isEqualTo(typeWithPokemons[0].pokemons[0].name)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

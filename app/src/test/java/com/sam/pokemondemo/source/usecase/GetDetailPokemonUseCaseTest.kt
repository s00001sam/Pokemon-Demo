package com.sam.pokemondemo.source.usecase

import com.google.common.truth.Truth.assertThat
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.DetailPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonWithTypes
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
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

class GetDetailPokemonUseCaseTest {
    private lateinit var repo: BaseRepository
    private lateinit var useCase: GetDetailPokemonUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        useCase = GetDetailPokemonUseCase(repo)
    }

    /**
     * Test call method in repo
     * - mock repo.getLocalDetailWithTypes() output
     * - trigger useCase invoke()
     * - Confirmed: check if calls method getLocalDetailWithTypes in repo
     * - Confirmed: the output data (name)
     * - Confirmed: the output data (description)
     * - Confirmed: the output data (typeNames.size)
     * - Confirmed: the output data (typeNames[0])
     */
    @Test
    fun `test call method getLocalDetailWithTypes in repo`() = runTest {
        val detailPokemonWithTypes = DetailPokemonWithTypes(
            pokemon = DetailPokemonView(
                pokemonId = 1,
                name = "pokemon1",
                description = "description1",
            ),
            typeRefs = listOf(
                TypePokemonCrossRef(
                    typeName = "type1",
                    pokemonId = 1,
                ),
                TypePokemonCrossRef(
                    typeName = "type2",
                    pokemonId = 1,
                ),
            )
        )
        every { repo.getLocalDetailWithTypes(1) } returns flowOf(detailPokemonWithTypes)

        val result = useCase.invoke(1).first()

        coVerify { repo.getLocalDetailWithTypes(1) }
        assertThat(result.name).isEqualTo(detailPokemonWithTypes.pokemon.name)
        assertThat(result.description).isEqualTo(detailPokemonWithTypes.pokemon.description)
        assertThat(result.typeNames.size).isEqualTo(2)
        assertThat(result.typeNames[0]).isEqualTo(detailPokemonWithTypes.typeRefs[0].typeName)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

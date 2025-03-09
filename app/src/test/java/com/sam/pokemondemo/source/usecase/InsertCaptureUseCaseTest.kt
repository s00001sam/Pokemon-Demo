package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class InsertCaptureUseCaseTest {
    private lateinit var repo: BaseRepository
    private lateinit var useCase: InsertCaptureUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        useCase = InsertCaptureUseCase(repo)
    }

    /**
     * Test call method in repo
     * - trigger useCase invoke() to capture 1st Pokemon in the database
     * - Confirmed: check if calls method insertCapture in repo
     */
    @Test
    fun `test call method insertCapture in repo`() = runTest {
        val capture = CaptureEntity(
            id = 1,
            pokemonId = 1,
            capturedTime = System.currentTimeMillis(),
        )
        useCase.invoke(capture)
        coVerify { repo.insertCapture(capture) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

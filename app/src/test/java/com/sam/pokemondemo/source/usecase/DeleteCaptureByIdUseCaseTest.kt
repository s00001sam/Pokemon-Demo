package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.source.repo.BaseRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeleteCaptureByIdUseCaseTest {
    private lateinit var repo: BaseRepository
    private lateinit var useCase: DeleteCaptureByIdUseCase

    @Before
    fun setup() {
        repo = mockk<BaseRepository>(relaxed = true)
        useCase = DeleteCaptureByIdUseCase(repo)
    }

    /**
     * Test call method in repo
     * - trigger useCase invoke(1) to delete capture with id 1
     * - Confirmed: check if calls method deleteCaptureById in repo
     */
    @Test
    fun `test call method deleteCaptureById in repo`() = runTest {
        useCase.invoke(1)
        coVerify { repo.deleteCaptureById(1) }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}

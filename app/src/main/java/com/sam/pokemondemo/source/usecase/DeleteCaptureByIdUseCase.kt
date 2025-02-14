package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.source.repo.BaseRepository
import javax.inject.Inject

class DeleteCaptureByIdUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    suspend fun invoke(captureId: Int) {
        repo.deleteCaptureById(captureId)
    }
}

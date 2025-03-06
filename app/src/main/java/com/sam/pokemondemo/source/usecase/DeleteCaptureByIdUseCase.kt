package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.source.repo.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteCaptureByIdUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    suspend fun invoke(captureId: Int) {
        withContext(Dispatchers.IO) {
            repo.deleteCaptureById(captureId)
        }
    }
}

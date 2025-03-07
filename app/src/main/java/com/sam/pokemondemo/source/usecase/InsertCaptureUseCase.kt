package com.sam.pokemondemo.source.usecase

import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import javax.inject.Inject

class InsertCaptureUseCase @Inject constructor(
    private val repo: BaseRepository,
) {
    suspend fun invoke(capture: CaptureEntity) {
        repo.insertCapture(capture)
    }
}

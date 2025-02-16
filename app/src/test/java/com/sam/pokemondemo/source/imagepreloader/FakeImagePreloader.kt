package com.sam.pokemondemo.source.imagepreloader

class FakeImagePreloader : ImagePreloader {
    // Represents the downloaded images
    val downloadImages = mutableListOf<String>()

    fun clear() {
        downloadImages.clear()
    }

    override suspend fun load(imageUrls: List<String>) {
        downloadImages.addAll(imageUrls)
    }
}

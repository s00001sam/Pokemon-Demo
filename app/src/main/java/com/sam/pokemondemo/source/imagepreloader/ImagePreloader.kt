package com.sam.pokemondemo.source.imagepreloader

interface ImagePreloader {
    suspend fun load(imageUrls: List<String>)
    suspend fun clear()
}

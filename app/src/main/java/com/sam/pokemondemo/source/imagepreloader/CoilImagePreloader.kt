package com.sam.pokemondemo.source.imagepreloader

import android.content.Context
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CoilImagePreloader @Inject constructor(
    @ApplicationContext private val context: Context,
) : ImagePreloader {
    override suspend fun load(imageUrls: List<String>) {
        imageUrls.forEach { url ->
            val request = ImageRequest.Builder(context)
                .data(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()

            context.imageLoader.enqueue(request)
        }
    }
}

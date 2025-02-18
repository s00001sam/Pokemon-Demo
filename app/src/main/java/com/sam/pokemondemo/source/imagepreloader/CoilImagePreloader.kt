package com.sam.pokemondemo.source.imagepreloader

import android.content.Context
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.sam.pokemondemo.source.room.PokemonDatabase
import com.sam.pokemondemo.source.room.entity.ImageCacheEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoilImagePreloader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: PokemonDatabase,
) : ImagePreloader {
    override suspend fun load(imageUrls: List<String>) {
        val imageCacheDao = db.imageCacheDao()
        val cacheList = imageCacheDao.getCacheList()

        imageUrls.forEach { url ->
            if (cacheList.contains(url)) return@forEach

            val request = ImageRequest.Builder(context)
                .data(url)
                .diskCachePolicy(CachePolicy.ENABLED)
                .listener(
                    onSuccess = { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            db.imageCacheDao().insertOne(ImageCacheEntity(url))
                        }
                    }
                )
                .build()

            context.imageLoader.enqueue(request)
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    override suspend fun clear() {
        db.imageCacheDao().clear()
        context.imageLoader.diskCache?.clear()
        context.imageLoader.memoryCache?.clear()
    }
}

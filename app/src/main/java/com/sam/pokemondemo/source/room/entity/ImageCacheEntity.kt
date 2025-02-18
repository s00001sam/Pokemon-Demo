package com.sam.pokemondemo.source.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "imageCache")
@Parcelize
data class ImageCacheEntity(
    @PrimaryKey
    val imageUrl: String = "",
) : Parcelable

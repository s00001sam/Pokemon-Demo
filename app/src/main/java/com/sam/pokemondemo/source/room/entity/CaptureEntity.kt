package com.sam.pokemondemo.source.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "capture")
@Parcelize
data class CaptureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pokemonId: Int = -1,
    val capturedTime: Long = 0L,
) : Parcelable

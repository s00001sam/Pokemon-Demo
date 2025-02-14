package com.sam.pokemondemo.source.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sam.pokemondemo.model.TypeResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = "type")
@Parcelize
data class TypeEntity(
    @PrimaryKey
    val name: String = UNKNOWN_TYPE_NAME,
) : Parcelable {
    companion object {
        const val UNKNOWN_TYPE_NAME = "unknown"

        private fun TypeResponse.toTypeEntity(): TypeEntity {
            return TypeEntity(
                name = type?.name.orEmpty().takeIf { it.isNotEmpty() } ?: UNKNOWN_TYPE_NAME,
            )
        }

        fun List<TypeResponse>?.toTypeEntities(): List<TypeEntity> {
            return this?.map { it.toTypeEntity() }
                .takeIf { !it.isNullOrEmpty() } ?: listOf(TypeEntity(name = UNKNOWN_TYPE_NAME))
        }
    }
}

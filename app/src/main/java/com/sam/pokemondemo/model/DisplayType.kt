package com.sam.pokemondemo.model

import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity.Companion.UNKNOWN_TYPE_NAME

data class DisplayType(
    val name: String = UNKNOWN_TYPE_NAME,
) {
    companion object {
        fun TypeEntity.toDisplayType(): DisplayType {
            return DisplayType(
                name = name,
            )
        }
    }
}

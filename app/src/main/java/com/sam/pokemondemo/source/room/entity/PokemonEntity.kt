package com.sam.pokemondemo.source.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sam.pokemondemo.model.RemotePokemonResponse
import kotlinx.parcelize.Parcelize

@Entity(tableName = "pokemon")
@Parcelize
data class PokemonEntity(
    @PrimaryKey
    val id: Int = -1,
    val name: String = "",
    val imageUrl: String = "",
    @ColumnInfo(defaultValue = "")
    val evolvesFromName: String = "",
    @ColumnInfo(defaultValue = "")
    val description: String = "",
) : Parcelable {
    companion object {
        fun BasicPokemonInfos.toPokemonEntity(): PokemonEntity {
            return PokemonEntity(
                id = id,
                name = name,
                imageUrl = imageUrl,
            )
        }
    }
}

data class BasicPokemonInfos(
    val id: Int = -1,
    val name: String = "",
    val imageUrl: String = "",
) {
    companion object {
        fun RemotePokemonResponse.getBasicPokemonInfos(): BasicPokemonInfos {
            return BasicPokemonInfos(
                id = id ?: 0,
                name = name.orEmpty(),
                imageUrl = sprites?.other?.officialArtwork?.frontDefault.orEmpty(),
            )
        }
    }
}

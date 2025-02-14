package com.sam.pokemondemo.source.room

import androidx.room.TypeConverter
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonConverters {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private inline fun <reified T> convertJsonToObject(json: String): T? =
        moshi.adapter(T::class.java).fromJson(json)

    private inline fun <reified T> convertObjectToJson(data: T?): String? =
        moshi.adapter(T::class.java).toJson(data)

    @TypeConverter
    fun fromString(value: String): List<String>? {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(type)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        return moshi.adapter(List::class.java).toJson(list)
    }

    @TypeConverter
    fun convertJsonToLocalType(json: String): TypeEntity? = convertJsonToObject<TypeEntity>(json)

    @TypeConverter
    fun convertLocalTypeToJson(data: TypeEntity?): String =
        convertObjectToJson<TypeEntity>(data).orEmpty()

    @TypeConverter
    fun fromLocalTypeString(value: String): List<TypeEntity>? {
        val type = Types.newParameterizedType(List::class.java, TypeEntity::class.java)
        val adapter: JsonAdapter<List<TypeEntity>> = moshi.adapter(type)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun fromLocalTypeList(list: List<TypeEntity>): String {
        return moshi.adapter(List::class.java).toJson(list)
    }
}

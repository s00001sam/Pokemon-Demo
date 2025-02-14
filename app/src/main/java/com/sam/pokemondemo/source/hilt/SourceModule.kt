package com.sam.pokemondemo.source.hilt

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sam.pokemondemo.source.apiservice.PokemonApiService
import com.sam.pokemondemo.source.datasource.BaseDataSource
import com.sam.pokemondemo.source.datasource.LocalDataSource
import com.sam.pokemondemo.source.datasource.RemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SourceModule {
    @Singleton
    @Provides
    fun provideMoshi() = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient) = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    @Singleton
    @Provides
    fun providePokemonApiService(retrofit: Retrofit) = retrofit.create(
        /* service = */ PokemonApiService::class.java
    )

    @Singleton
    @LocalData
    @Provides
    fun providerLocalDataSource(): BaseDataSource {
        return LocalDataSource()
    }

    @Singleton
    @RemoteData
    @Provides
    fun providerRemoteDataSource(apiService: PokemonApiService): BaseDataSource {
        return RemoteDataSource(apiService)
    }

    companion object {
        private const val BASE_URL = "https://pokeapi.co/api/v2/"
    }
}

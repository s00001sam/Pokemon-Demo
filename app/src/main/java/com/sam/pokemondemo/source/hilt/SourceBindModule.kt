package com.sam.pokemondemo.source.hilt

import com.sam.pokemondemo.source.imagepreloader.CoilImagePreloader
import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.repo.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SourceBindModule {
    @Binds
    @Singleton
    abstract fun bindPokemonRepository(impl: PokemonRepository): BaseRepository

    @Binds
    @Singleton
    abstract fun bindCoilImagePreloader(impl: CoilImagePreloader): ImagePreloader
}

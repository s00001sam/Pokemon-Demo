package com.sam.pokemondemo.source.hilt

import com.sam.pokemondemo.source.imagepreloader.ImagePreloader
import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.usecase.DeleteCaptureByIdUseCase
import com.sam.pokemondemo.source.usecase.GetCapturedPokemonsUseCase
import com.sam.pokemondemo.source.usecase.GetDetailPokemonUseCase
import com.sam.pokemondemo.source.usecase.GetTypeWithPokemonsUseCase
import com.sam.pokemondemo.source.usecase.InsertCaptureUseCase
import com.sam.pokemondemo.source.usecase.UpdatePokemonDetailFromRemoteUseCase
import com.sam.pokemondemo.source.usecase.UpdatePokemonsFromRemoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class UseCaseModule {
    @Provides
    fun provideUpdatePokemonsFromRemoteUseCase(
        repository: BaseRepository,
        imagePreloader: ImagePreloader,
    ) = UpdatePokemonsFromRemoteUseCase(repository, imagePreloader)

    @Provides
    fun provideGetTypeWithPokemonsUseCase(
        repository: BaseRepository,
    ) = GetTypeWithPokemonsUseCase(repository)

    @Provides
    fun provideGetCapturedPokemonsUseCase(
        repository: BaseRepository,
    ) = GetCapturedPokemonsUseCase(repository)

    @Provides
    fun provideInsertCaptureUseCase(
        repository: BaseRepository,
    ) = InsertCaptureUseCase(repository)

    @Provides
    fun provideDeleteCaptureByIdUseCase(
        repository: BaseRepository,
    ) = DeleteCaptureByIdUseCase(repository)

    @Provides
    fun provideUpdatePokemonDetailFromRemoteUseCase(
        repository: BaseRepository,
        imagePreloader: ImagePreloader,
    ) = UpdatePokemonDetailFromRemoteUseCase(repository, imagePreloader)

    @Provides
    fun provideGetDetailPokemonUseCase(
        repository: BaseRepository,
    ) = GetDetailPokemonUseCase(repository)
}

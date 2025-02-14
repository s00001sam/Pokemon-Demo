package com.sam.pokemondemo.source.hilt

import com.sam.pokemondemo.source.repo.BaseRepository
import com.sam.pokemondemo.source.usecase.GetTypeWithPokemonsUseCase
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
    ) = UpdatePokemonsFromRemoteUseCase(repository)

    @Provides
    fun provideGetTypeWithPokemonsUseCase(
        repository: BaseRepository,
    ) = GetTypeWithPokemonsUseCase(repository)
}

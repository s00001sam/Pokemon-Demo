package com.sam.pokemondemo.source.repo

import com.sam.pokemondemo.source.datasource.BaseDataSource
import com.sam.pokemondemo.source.hilt.LocalData
import com.sam.pokemondemo.source.hilt.RemoteData
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    @LocalData private val localDataSource: BaseDataSource,
    @RemoteData private val remoteDataSource: BaseDataSource,
) : BaseRepository {

}

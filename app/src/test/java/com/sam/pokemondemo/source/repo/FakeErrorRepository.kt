package com.sam.pokemondemo.source.repo

import com.sam.pokemondemo.model.BasicPokemonsResponse
import com.sam.pokemondemo.model.PokemonSpeciesResponse
import com.sam.pokemondemo.model.RemotePokemonResponse
import com.sam.pokemondemo.source.getAllTypePokemonCrossRefs
import com.sam.pokemondemo.source.mockPokemons
import com.sam.pokemondemo.source.mockTypes
import com.sam.pokemondemo.source.room.entity.BasicPokemonInfos
import com.sam.pokemondemo.source.room.entity.CaptureEntity
import com.sam.pokemondemo.source.room.entity.CapturedPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonView
import com.sam.pokemondemo.source.room.entity.DetailPokemonWithTypes
import com.sam.pokemondemo.source.room.entity.PokemonEntity
import com.sam.pokemondemo.source.room.entity.PokemonEntity.Companion.toPokemonEntity
import com.sam.pokemondemo.source.room.entity.TypeEntity
import com.sam.pokemondemo.source.room.entity.TypePokemonCrossRef
import com.sam.pokemondemo.source.room.entity.TypeWithPokemons
import com.sam.pokemondemo.source.toBasicPokemonEntities
import com.sam.pokemondemo.source.toPokemonEntity
import com.sam.pokemondemo.source.toTypeEntities
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class FakeErrorRepository : BaseRepository {
    val currPokemons = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val currTypes = MutableStateFlow<List<TypeEntity>>(emptyList())
    val currCaptures = MutableStateFlow<List<CaptureEntity>>(emptyList())
    val currTypePokemonCrossRefs = MutableStateFlow<List<TypePokemonCrossRef>>(emptyList())

    fun clear() {
        currPokemons.tryEmit(emptyList())
        currTypes.tryEmit(emptyList())
        currCaptures.tryEmit(emptyList())
        currTypePokemonCrossRefs.tryEmit(emptyList())
    }

    // Initialize the homepage data
    fun initAllBasicData() {
        currPokemons.tryEmit(mockPokemons.toBasicPokemonEntities())
        currTypes.tryEmit(mockTypes.toTypeEntities())
        currTypePokemonCrossRefs.tryEmit(getAllTypePokemonCrossRefs())
    }

    // Initialize the detail page data
    fun initDetailsData(pokemonId: Int) {
        val pokemons = currPokemons.value.toMutableList()
        val index = currPokemons.value.indexOfFirst { it.id == pokemonId }
        val entity = mockPokemons.find { it.id == pokemonId }?.toPokemonEntity() ?: return
        when (index) {
            -1 -> {
                pokemons.add(entity)
            }

            else -> {
                pokemons[index] = entity
            }
        }
    }

    override suspend fun getRemoteBasicPokemons(url: String): Response<BasicPokemonsResponse> {
        delay(500L)
        return Response.error(400, "Unknown error".toResponseBody())
    }

    override suspend fun getRemotePokemon(url: String): Response<RemotePokemonResponse> {
        delay(100L)
        return Response.error(400, "Unknown error".toResponseBody())
    }

    override suspend fun getRemotePokemon(id: Int): Response<RemotePokemonResponse> {
        return Response.error(400, "Unknown error".toResponseBody())
    }

    override suspend fun getLocalPokemonNames(): List<String> {
        return currPokemons.value.map { it.name }
    }

    override suspend fun upsertBasicPokemonsAndTypes(
        basicInfos: List<BasicPokemonInfos>,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>
    ) {
        val tempPokemons = currPokemons.value.toMutableList()
        val tempRefs = currTypePokemonCrossRefs.value.toMutableSet()
        val tempTypes = currTypes.value.toMutableSet()

        basicInfos.forEach { basicInfo ->
            val index = tempPokemons.indexOfFirst { it.id == basicInfo.id }
            when (index) {
                -1 -> {
                    tempPokemons.add(basicInfo.toPokemonEntity())
                }

                else -> {
                    tempPokemons[index] = tempPokemons[index].copy(
                        id = basicInfo.id,
                        name = basicInfo.name,
                        imageUrl = basicInfo.imageUrl,
                    )
                }
            }
        }

        tempRefs.addAll(refs)
        tempTypes.addAll(types)

        currPokemons.tryEmit(tempPokemons)
        currTypePokemonCrossRefs.tryEmit(tempRefs.toList())
        currTypes.tryEmit(tempTypes.toList())
    }

    override fun getLocalTypeWithPokemons(): Flow<List<TypeWithPokemons>> {
        return combine(
            currTypes,
            currPokemons,
            currTypePokemonCrossRefs,
        ) { types, pokemons, refs ->
            types.map { type ->
                val filterPokemons = refs.filter { it.typeName == type.name }
                    .map { it.pokemonId }
                    .mapNotNull { id ->
                        pokemons.find { it.id == id }
                    }
                TypeWithPokemons(
                    type = type,
                    pokemons = filterPokemons,
                )
            }
        }
    }

    override suspend fun insertCapture(capture: CaptureEntity) {
        val new = currCaptures.value.toMutableList().apply {
            val id = this.size + 1
            add(capture.copy(id = id))
        }
        currCaptures.tryEmit(new)
    }

    override suspend fun deleteCaptureById(id: Int) {
        val new = currCaptures.value.toMutableList().apply {
            val capture = this.find { it.id == id } ?: return
            remove(capture)
        }
        currCaptures.tryEmit(new)
    }

    override fun getLocalCapturedPokemonsByTimeDesc(): Flow<List<CapturedPokemonView>> {
        return combine(
            currPokemons,
            currCaptures,
        ) { pokemons, captures ->
            captures.mapNotNull { capture ->
                val pokemon = pokemons.find { it.id == capture.pokemonId } ?: return@mapNotNull null
                CapturedPokemonView(
                    id = capture.id,
                    pokemonId = pokemon.id,
                    name = pokemon.name,
                    imageUrl = pokemon.imageUrl,
                    capturedTime = capture.capturedTime,
                )
            }.sortedByDescending { it.capturedTime }
        }
    }

    override suspend fun getRemotePokemonSpecies(id: Int): Response<PokemonSpeciesResponse> {
        return Response.error(400, "Unknown error".toResponseBody())
    }

    override suspend fun updateDetails(
        pokemonEntity: PokemonEntity,
        refs: List<TypePokemonCrossRef>,
        types: List<TypeEntity>
    ) {
        val tempPokemons = currPokemons.value.toMutableList()
        val tempRefs = currTypePokemonCrossRefs.value.toMutableSet()
        val tempTypes = currTypes.value.toMutableSet()

        val indexPokemon = tempPokemons.indexOfFirst { it.id == pokemonEntity.id }
        when (indexPokemon) {
            -1 -> {
                tempPokemons.add(pokemonEntity)
            }

            else -> {
                tempPokemons[indexPokemon] = pokemonEntity
            }
        }

        tempRefs.addAll(refs)
        tempTypes.addAll(types)

        currPokemons.tryEmit(tempPokemons)
        currTypePokemonCrossRefs.tryEmit(tempRefs.toList())
        currTypes.tryEmit(tempTypes.toList())
    }

    override fun getLocalDetailWithTypes(pokemonId: Int): Flow<DetailPokemonWithTypes> {
        return combine(
            currPokemons,
            currTypePokemonCrossRefs,
        ) { pokemons, refs ->
            val currPokemon = pokemons.find { it.id == pokemonId }
            val evolvesFrom = pokemons.find { it.name == currPokemon?.evolvesFromName }
            DetailPokemonWithTypes(
                pokemon = DetailPokemonView(
                    pokemonId = currPokemon?.id ?: -1,
                    name = currPokemon?.name.orEmpty(),
                    imageUrl = currPokemon?.imageUrl.orEmpty(),
                    description = currPokemon?.description.orEmpty(),
                    evolvesFromId = evolvesFrom?.id ?: -1,
                    evolvesFromName = evolvesFrom?.name.orEmpty(),
                    evolvesFromImageUrl = evolvesFrom?.imageUrl.orEmpty(),
                ),
                typeRefs = refs.filter { it.pokemonId == currPokemon?.id }
            )
        }
    }

    override suspend fun clearLocalDataWithoutCapture() {
        currPokemons.tryEmit(emptyList())
        currTypes.tryEmit(emptyList())
        currTypePokemonCrossRefs.tryEmit(emptyList())
    }
}

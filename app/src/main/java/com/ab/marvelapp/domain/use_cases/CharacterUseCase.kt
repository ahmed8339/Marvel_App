package com.ab.marvelapp.domain.use_cases

import com.ab.marvelapp.commons.Response
import com.ab.marvelapp.domain.model.Character
import com.ab.marvelapp.domain.repository.MarvelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterUseCase @Inject constructor(
    private val repository: MarvelRepository
) {

    operator fun invoke(offset: Int): Flow<Response<List<Character>>> = flow {
        try {
            emit(Response.Loading<List<Character>>())
            val list = repository.getAllCharacters(offset = offset).data.results.map {
                it.toCharacter()
            }
            emit(Response.Success<List<Character>>(list))
        } catch (e: HttpException) {
            emit(Response.Error<List<Character>>(e.printStackTrace().toString()))
        } catch (e: IOException) {
            emit(Response.Error<List<Character>>(e.printStackTrace().toString()))
        }
    }
}
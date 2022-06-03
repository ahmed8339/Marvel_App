package com.ab.marvelapp.data.repository

import com.ab.marvelapp.data.source.MarvelApi
import com.ab.marvelapp.data.source.dto.CharacterDTO
import com.ab.marvelapp.domain.repository.MarvelRepository
import javax.inject.Inject

class MarvelRepositoryImpl @Inject constructor(
    private val api:MarvelApi
):MarvelRepository {
    override suspend fun getAllCharacters(offset: Int): CharacterDTO {
        return api.getAllCharacters(offset = offset.toString())
    }

    override suspend fun getAllSearchCharacters(search: String): CharacterDTO {
        return api.getAllSearchedCharacters(search = search)
    }
}
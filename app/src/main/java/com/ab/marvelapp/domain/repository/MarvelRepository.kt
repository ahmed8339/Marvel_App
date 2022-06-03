package com.ab.marvelapp.domain.repository

import com.ab.marvelapp.data.source.dto.CharacterDTO

interface MarvelRepository {
    suspend fun getAllCharacters(offset:Int):CharacterDTO
    suspend fun getAllSearchCharacters(search:String):CharacterDTO
}
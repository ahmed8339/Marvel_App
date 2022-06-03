package com.ab.marvelapp.di

import com.ab.marvelapp.commons.Constants
import com.ab.marvelapp.data.repository.MarvelRepositoryImpl
import com.ab.marvelapp.data.source.MarvelApi
import com.ab.marvelapp.domain.repository.MarvelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun provideMarvelApi():MarvelApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMarvelRepository(api:MarvelApi):MarvelRepository{
        return MarvelRepositoryImpl(api)
    }
}
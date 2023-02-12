package com.example.ddtapp.di

import com.example.ddtapp.api.Api
import com.example.ddtapp.repository.HouseRepository
import com.example.ddtapp.repository.HouseRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    internal fun provideHouseRepository(api: Api): HouseRepository = HouseRepositoryImpl(api = api)

}
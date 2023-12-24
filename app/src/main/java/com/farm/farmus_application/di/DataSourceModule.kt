package com.farm.farmus_application.di

import com.farm.farmus_application.repository.farm.FarmDataSourceInterface
import com.farm.farmus_application.repository.farm.FarmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindFarmDataSource(farmDataSourceImpl: FarmRepository): FarmDataSourceInterface
}
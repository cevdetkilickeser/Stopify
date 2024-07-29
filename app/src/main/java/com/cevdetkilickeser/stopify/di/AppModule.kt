package com.cevdetkilickeser.stopify.di

import com.cevdetkilickeser.stopify.repo.ServiceRepository
import com.cevdetkilickeser.stopify.retrofit.ApiService
import com.cevdetkilickeser.stopify.retrofit.ApiUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideServiceRepository(apiService: ApiService): ServiceRepository {
        return ServiceRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiUtils.getApiService()
    }
}
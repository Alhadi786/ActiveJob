package com.xee.app.activejob.di

import com.xee.app.activejob.network.ApiService
import com.xee.app.activejob.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {


    @Singleton
    @Provides
    fun provideAuthApiService(): ApiService {
        val api by lazy {
            RetrofitClient.ServiceBuilder.buildService(
                ApiService::class.java,
            )
        }
        return api
    }
}
package com.xee.app.activejob.di

import android.content.Context
import androidx.room.Room
import com.xee.app.activejob.constants.DATABASE_NAME
import com.xee.app.activejob.db.AppDatabase
import com.xee.app.activejob.network.ApiKeyInterceptor
import com.xee.app.activejob.network.ApiService
import com.xee.app.activejob.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()


    @Singleton
    @Provides
    fun provideHeaderInterceptor() = ApiKeyInterceptor()

    @Singleton
    @Provides
    fun provideAuthApiService(apiKeyInterceptor: ApiKeyInterceptor): ApiService {
        val api by lazy {
            RetrofitClient.ServiceBuilder.buildService(
                ApiService::class.java,
                apiKeyInterceptor,
            )
        }
        return api
    }
}
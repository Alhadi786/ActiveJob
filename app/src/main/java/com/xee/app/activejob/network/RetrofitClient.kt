package com.xee.app.activejob.network

import com.google.gson.GsonBuilder
import com.xee.app.activejob.BuildConfig
import com.xee.app.activejob.constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    object ServiceBuilder {
        fun <T> buildService(apiInterface: Class<T>, apiKeyInterceptor: ApiKeyInterceptor): T {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG)
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            else interceptor.level = HttpLoggingInterceptor.Level.NONE

            val okHttpClient = OkHttpClient
                .Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(apiKeyInterceptor)
                .build()


            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return retrofit.create(apiInterface)

        }
    }
}
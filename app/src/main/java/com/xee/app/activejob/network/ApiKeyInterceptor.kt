package com.xee.app.activejob.network

import com.xee.app.activejob.constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder().addQueryParameter("apiKey",API_KEY).build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}
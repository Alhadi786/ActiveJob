package com.xee.app.activejob.network

import com.xee.app.activejob.constants.BASE_URL
import com.xee.app.activejob.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiService {
    @Headers("Accept: application/json")
    @GET
    suspend fun getMovies(@Url url:String="https://www.omdbapi.com?s=movie&page=1"): MoviesResponse
}
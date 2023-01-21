package com.xee.app.activejob.network

import com.xee.app.activejob.model.NearByPeopleResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface ApiService {
    @Headers("Accept: application/json")
    @GET
    suspend fun getNearByPeople(@Url url :String= "https://randomuser.me/api?results=300&exc=login,registered,dob,phone,id,gender"): NearByPeopleResponse
}
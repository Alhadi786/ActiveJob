package com.xee.app.activejob.model


import com.google.gson.annotations.SerializedName

data class NearByPeopleResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val people: ArrayList<Person>
)
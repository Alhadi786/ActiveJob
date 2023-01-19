package com.xee.app.activejob.model


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("cell")
    val cell: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: Name,
    @SerializedName("nat")
    val nat: String,
    @SerializedName("picture")
    val picture: Picture
)
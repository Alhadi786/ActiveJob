package com.xee.app.activejob.model


import com.google.gson.annotations.SerializedName
import com.xee.app.activejob.uitils.distanceBetween

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
){
     var distance: Float = 0f


    fun checkIfNearBy(currentLocation: android.location.Location): Boolean {

         distance = currentLocation.distanceBetween(location.coordinates.latitude , location.coordinates.longitude)
        if (distance < 4000)
            return true

        return false
    }
}
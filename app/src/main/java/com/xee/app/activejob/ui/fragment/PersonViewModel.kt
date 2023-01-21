package com.xee.app.activejob.ui.fragment

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.xee.app.activejob.model.Coordinates
import com.xee.app.activejob.network.ApiHandler
import com.xee.app.activejob.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    private val locations = arrayListOf<LatLng>()
 init {
     locations.add(LatLng(52.960528, -8.034647))
     locations.add(LatLng(28.185491, 85.313264))
     locations.add(LatLng(56.444461, 9.547368))
     locations.add(LatLng(57.301260, 15.512887))
     locations.add(LatLng(20.735937, 78.064859))
     locations.add(LatLng(52.395963, 10.497796))
     locations.add(LatLng(34.790598, 56.804239))
     locations.add(LatLng(21.139428, 47.344256))
     locations.add(LatLng(37.679856, -91.330650))
     locations.add(LatLng(28.668491, 116.052151))
     locations.add(LatLng(28.641676, 66.991620))
 }
    val randomLocation = MutableLiveData<Location>()
    fun getNearByPeople() = liveData {
        val response = ApiHandler.safeApiCall {
            apiService.getNearByPeople()
        }
        emit(response)
    }

    fun randomizeLocation() {
        viewModelScope.launch {
            while (this.isActive) {
                val randomLoc = getRandomLocation()
                randomLocation.postValue(randomLoc)
                delay(5000)
            }
        }
    }


    private fun getRandomLocation(): Location {
        val random = Random()
        val index = random.nextInt(locations.size)
        val randomCoordinates = locations[index]
        val currentLocation = Location("")

        currentLocation.latitude = randomCoordinates.latitude
        currentLocation.longitude = randomCoordinates.longitude

        return currentLocation

    }
}
package com.xee.app.activejob.ui.fragment

import androidx.lifecycle.*
import com.xee.app.activejob.model.Person
import com.xee.app.activejob.network.ApiHandler
import com.xee.app.activejob.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {

    fun getNearByPeople() = liveData {
        val response = ApiHandler.safeApiCall {
            apiService.getNearByPeople()
        }
        emit(response)
    }
}
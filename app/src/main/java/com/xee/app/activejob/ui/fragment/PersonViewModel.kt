package com.xee.app.activejob.ui.fragment

import androidx.lifecycle.*
import com.xee.app.activejob.model.Person
import com.xee.app.activejob.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {

    val users = MutableLiveData<ArrayList<Person>>()
    init {

        viewModelScope.launch {
            while (this.isActive){
              val data = apiService.getNearByPeople()
                users.postValue(data.people)
                delay(5000)
            }
        }

    }


}
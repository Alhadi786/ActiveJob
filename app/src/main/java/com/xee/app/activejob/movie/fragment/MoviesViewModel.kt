package com.xee.app.activejob.movie.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.xee.app.activejob.db.AppDatabase
import com.xee.app.activejob.model.Search
import com.xee.app.activejob.network.ApiHandler
import com.xee.app.activejob.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
) : ViewModel() {
    fun getMovies() = liveData {
        val response = ApiHandler.safeApiCall {
            apiService.getMovies()
        }
        emit(response)
    }

    fun storeAllMovies(movies: ArrayList<Search>) {
        viewModelScope.launch {
            movies.forEach {
                db.getMovieDao().insert(it)
            }
        }
    }

    fun getAllMoviesFromDB() : LiveData<List<Search>>?{
        return  db.getMovieDao().getAllMovies()
    }

    fun updateMovie(search: Search) = liveData(Dispatchers.IO){
        val response = db.getMovieDao().updateRecord(search)
        emit(response)
    }
}
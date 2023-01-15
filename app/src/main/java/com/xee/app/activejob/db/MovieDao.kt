package com.xee.app.activejob.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.xee.app.activejob.model.Search


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(search: Search)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): LiveData<List<Search>>?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecord(search: Search):Int

}

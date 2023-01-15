package com.xee.app.activejob.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xee.app.activejob.model.Search

@Database(entities = [Search::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

}

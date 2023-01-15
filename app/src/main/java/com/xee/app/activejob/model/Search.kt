package com.xee.app.activejob.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "movies"
)
data class Search(
    val Poster: String,
    var Title: String,
    val Type: String,
    var Year: String,
    @PrimaryKey
    val imdbID: String,
): Serializable{
    @Ignore
    var viewType: Int=1
}
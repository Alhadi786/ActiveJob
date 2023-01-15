package com.xee.app.activejob.model

data class MoviesResponse(
    val Response: Boolean,
    val Search: ArrayList<Search>,
    val totalResults: Int
)
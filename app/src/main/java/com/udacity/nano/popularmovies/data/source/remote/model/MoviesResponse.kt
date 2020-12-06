package com.udacity.nano.popularmovies.data.source.remote.model

import com.udacity.nano.popularmovies.data.source.PopularMovie

data class MoviesResponse(
    val page: Int,
    val results: List<PopularMovie>?,
    val total_pages: Int,
    val total_results: Int,
)
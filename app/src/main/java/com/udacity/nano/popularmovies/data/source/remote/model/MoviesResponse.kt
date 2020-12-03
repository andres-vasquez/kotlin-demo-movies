package com.udacity.nano.popularmovies.data.source.remote.model

import com.udacity.nano.popularmovies.data.source.local.MovieDTO

data class MoviesResponse @JvmOverloads constructor(
    val page: Int,
    val results: List<MovieDTO>,
    val total_pages: Int,
    val total_results: Int,
)
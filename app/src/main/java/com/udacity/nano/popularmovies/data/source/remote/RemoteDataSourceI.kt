package com.udacity.nano.popularmovies.data.source.remote

import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.PopularMovie

interface RemoteDataSourceI {
    suspend fun getPopularMovies(lang: String): Result<List<PopularMovie>>
}
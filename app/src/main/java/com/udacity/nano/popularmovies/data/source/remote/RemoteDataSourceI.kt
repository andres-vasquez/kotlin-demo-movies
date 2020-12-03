package com.udacity.nano.popularmovies.data.source.remote

import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.Result

interface RemoteDataSourceI {
    suspend fun getPopularMovies(): Result<List<MovieDTO>>
}
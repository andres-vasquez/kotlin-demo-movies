package com.udacity.nano.popularmovies.data.source.remote

import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.remote.model.Genre

interface RemoteDataSourceI {
    suspend fun getPopularMovies(lang: String): Result<List<PopularMovie>>
    suspend fun getGenres(lang: String): Result<List<Genre>>
}
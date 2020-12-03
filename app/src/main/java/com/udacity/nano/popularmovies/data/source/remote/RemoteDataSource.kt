package com.udacity.nano.popularmovies.data.source.remote

import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.source.local.MoviesDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.lang.NullPointerException

class RemoteDataSource internal constructor(
    private val moviesApi: MoviesApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSourceI {

    override suspend fun getPopularMovies(): Result<List<MovieDTO>> {
        val response = moviesApi.retrofitService.getPopularMovies().await()
        return if (response.results.isNullOrEmpty()) {
            Result.Error(NullPointerException("No movies available"))
        } else {
            Result.Success(response.results)
        }
    }
}
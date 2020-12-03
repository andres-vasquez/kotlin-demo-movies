package com.udacity.nano.popularmovies.data.source.remote

import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.PopularMovie
import retrofit2.HttpException
import java.lang.NullPointerException

class RemoteDataSource internal constructor(
) : RemoteDataSourceI {

    override suspend fun getPopularMovies(): Result<List<PopularMovie>> {
        return try {
            val response = MoviesApi.retrofitService.getPopularMovies().await()
            return if (response.results.isNullOrEmpty()) {
                Result.Error(NullPointerException("No movies available"))
            } else {
                Result.Success(response.results)
            }
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }
}
package com.udacity.nano.popularmovies.data.source.remote

import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.remote.model.Genre
import retrofit2.HttpException

class RemoteDataSource internal constructor(
) : RemoteDataSourceI {

    override suspend fun getPopularMovies(lang: String, page: Int): Result<List<PopularMovie>> {
        return try {
            val response = MoviesApi.retrofitService.getPopularMovies(lang, page)
            return if (response.results.isNullOrEmpty()) {
                Result.Error(NullPointerException("No movies available"))
            } else {
                Result.Success(response.results)
            }
            //Result.Success(listOf())
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getGenres(lang: String): Result<List<Genre>> {
        return try {
            val response = MoviesApi.retrofitService.getGenres(lang)
            return if (!response.genres.isNullOrEmpty()) {
                Result.Success(response.genres)
            } else {
                Result.Success(listOf())
            }
            //Result.Success(listOf())
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
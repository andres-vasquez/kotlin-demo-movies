package com.udacity.nano.popularmovies.data.source.local

import androidx.lifecycle.LiveData
import com.udacity.nano.popularmovies.data.Result

interface LocalDataSourceI {
    fun observeMovies(): LiveData<Result<List<MovieDTO>>>

    suspend fun getMovieById(movieId: Int): Result<MovieDTO>

    suspend fun insertMovies(vararg movies: MovieDTO)

    suspend fun deleteMovies()
}
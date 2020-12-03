package com.udacity.nano.popularmovies.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.nano.popularmovies.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource internal constructor(
    private val moviesDao: MoviesDAO,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSourceI {

    override fun observeMovies(): LiveData<Result<List<MovieDTO>>> {
        return moviesDao.observeMovies().map {
            Result.Success(it)
        }
    }

    override suspend fun getMovieById(movieId: Int): Result<MovieDTO> = withContext(ioDispatcher) {
        try {
            val movie = moviesDao.getMovieById(movieId)
            if (movie != null) {
                return@withContext Result.Success(movie)
            } else {
                return@withContext Result.Error(Exception("Movie not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun insertMovies(movies: List<MovieDTO>) = withContext(ioDispatcher) {
        moviesDao.insertMovies(movies)
    }

    override suspend fun deleteMovies() = withContext(ioDispatcher) {
        moviesDao.deleteMovies()
    }
}
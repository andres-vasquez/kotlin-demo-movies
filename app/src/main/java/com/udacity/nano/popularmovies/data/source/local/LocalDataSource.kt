package com.udacity.nano.popularmovies.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingSource
import com.example.android.codelabs.paging.db.RemoteKeys
import com.udacity.nano.popularmovies.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource internal constructor(
    private val database: MoviesDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSourceI {

    override fun getMovies(): PagingSource<Int, MovieDTO> {
        return database.moviesDao().getMovies()
    }


    override suspend fun getMovieById(movieId: Int): Result<MovieDTO> = withContext(ioDispatcher) {
        try {
            val movie = database.moviesDao().getMovieById(movieId)
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
        database.moviesDao().insertMovies(movies)
    }

    override suspend fun deleteMovies() = withContext(ioDispatcher) {
        database.moviesDao().deleteMovies()
    }

    override suspend fun insertAllRemoteKeys(remoteKey: List<RemoteKeys>) {
        database.remoteKeysDao().insertAll(remoteKey)
    }

    override suspend fun remoteKeysMovieId(movieId: Long): RemoteKeys? {
        return database.remoteKeysDao().remoteKeysRepoId(movieId)
    }

    override suspend fun clearRemoteKeys() {
        database.remoteKeysDao().clearRemoteKeys()
    }
}
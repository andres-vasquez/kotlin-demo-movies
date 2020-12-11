package com.udacity.nano.popularmovies.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingSource
import com.example.android.codelabs.paging.db.RemoteKeys
import com.udacity.nano.popularmovies.data.Result

interface LocalDataSourceI {
    //Movies
    fun getMovies(): PagingSource<Int, MovieDTO>
    suspend fun getMovieById(movieId: Int): Result<MovieDTO>
    suspend fun insertMovies(movies: List<MovieDTO>)
    suspend fun deleteMovies()

    //Remote keys
    suspend fun insertAllRemoteKeys(remoteKey: List<RemoteKeys>)
    suspend fun remoteKeysMovieId(movieId: Long): RemoteKeys?
    suspend fun clearRemoteKeys()
}
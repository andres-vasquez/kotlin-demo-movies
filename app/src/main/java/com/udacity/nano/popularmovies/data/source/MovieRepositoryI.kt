package com.udacity.nano.popularmovies.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus
import com.udacity.nano.popularmovies.data.source.remote.model.Genre
import kotlinx.coroutines.flow.Flow

interface MovieRepositoryI {
    fun getUserPrefs(): User?
    fun saveUserPrefs(user: User)
    suspend fun getGenres(genres: Set<Int>): List<Genre>
    suspend fun refreshData()
    fun getPagingMovies(): Flow<PagingData<MovieDTO>>

    val status: LiveData<ApiStatus>
}
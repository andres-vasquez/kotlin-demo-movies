package com.udacity.nano.popularmovies.data.source

import androidx.lifecycle.LiveData
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus
import com.udacity.nano.popularmovies.data.source.remote.model.Genre

interface MovieRepositoryI {
    fun getUserPrefs(): User?
    fun saveUserPrefs(user: User)
    suspend fun getGenres(genres: Set<Int>): List<Genre>
    suspend fun refreshData()

    val status: LiveData<ApiStatus>
    val movies: LiveData<List<PopularMovie>>
}
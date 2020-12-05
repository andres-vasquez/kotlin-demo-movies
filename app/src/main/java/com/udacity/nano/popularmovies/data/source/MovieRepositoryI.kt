package com.udacity.nano.popularmovies.data.source

import androidx.lifecycle.LiveData
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus

interface MovieRepositoryI {
    fun getUserPrefs(): User?
    fun saveUserPrefs(user: User)
    suspend fun refreshData()

    val status: LiveData<ApiStatus>
    val movies: LiveData<List<PopularMovie>>
}
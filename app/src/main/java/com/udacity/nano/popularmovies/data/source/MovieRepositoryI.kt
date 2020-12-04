package com.udacity.nano.popularmovies.data.source

interface MovieRepositoryI {
    fun getUserPrefs(): User?
    fun saveUserPrefs(user: User)
    suspend fun refreshData()
}
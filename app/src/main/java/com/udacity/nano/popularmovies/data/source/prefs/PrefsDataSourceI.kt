package com.udacity.nano.popularmovies.data.source.prefs

import com.udacity.nano.popularmovies.data.source.User

interface PrefsDataSourceI {
    fun getUserPrefs(): User?
    fun saveUserPrefs(user: User)
    fun getLanguage(): String
}
package com.udacity.nano.popularmovies.data.source.prefs

import android.app.Application
import android.content.Context
import com.udacity.nano.popularmovies.data.source.User
import com.udacity.nano.popularmovies.utils.Constants

class PrefsDataSource(val app: Application) : PrefsDataSourceI {

    override fun getUserPrefs(): User? {
        val displayName = getPref(Constants.PREFS_DISPLAY_NAME)
        if (displayName.isNullOrEmpty()) {
            return null
        }
        return User(
            displayName,
            getPref(Constants.PREFS_PHOTO),
            getPref(Constants.PREFS_LANGUAGE)
        )
    }

    override fun saveUserPrefs(user: User) {
        savePref(Constants.PREFS_DISPLAY_NAME, user.displayName)
        user.photo?.let { savePref(Constants.PREFS_PHOTO, it) }
        user.language?.let { savePref(Constants.PREFS_LANGUAGE, it) }
    }

    private fun savePref(key: String, value: String) {
        val sharedPref = app.getSharedPreferences(Constants.SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getPref(key: String): String? {
        val sharedPref = app.getSharedPreferences(Constants.SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString(key, null)
    }
}
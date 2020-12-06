package com.udacity.nano.popularmovies.ui.login

import com.udacity.nano.popularmovies.data.source.User

fun validateUser(user: User): Boolean {
    return !user.displayName.isNullOrEmpty()
}
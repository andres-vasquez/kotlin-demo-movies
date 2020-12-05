package com.udacity.nano.popularmovies.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.data.source.User
import com.udacity.nano.popularmovies.ui.base.BaseViewModel

class LoginViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

    private val _editPhoto = MutableLiveData<Boolean>()
    val editPhoto: LiveData<Boolean> get() = _editPhoto

    init {
        _editPhoto.value = false
    }

    fun editPhoto() {
        _editPhoto.value = true
    }

    fun doneEditingPhoto() {
        _editPhoto.value = false
    }

    fun validateAndSave(user: User) {

    }
}
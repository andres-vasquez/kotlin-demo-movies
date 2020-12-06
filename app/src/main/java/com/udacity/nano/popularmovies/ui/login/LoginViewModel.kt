package com.udacity.nano.popularmovies.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.nano.popularmovies.R
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.data.source.User
import com.udacity.nano.popularmovies.ui.base.BaseViewModel
import com.udacity.nano.popularmovies.ui.base.NavigationCommand

class LoginViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

    private val _editPhoto = MutableLiveData<Boolean>()
    val editPhoto: LiveData<Boolean> get() = _editPhoto

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        _editPhoto.value = false
        loadUser()
    }

    private fun loadUser() {
        val user = repository.getUserPrefs()
        if (user != null) {
            _currentUser.value = user!!
        } else {
            _currentUser.value = User("", null, null)
        }
    }

    fun editPhoto() {
        _editPhoto.value = true
    }

    fun doneEditingPhoto() {
        _editPhoto.value = false
    }

    fun validateAndSave(user: User) {
        if (validateUser(user)) {
            repository.saveUserPrefs(user)
            navigationCommand.value = NavigationCommand.To(
                LoginFragmentDirections.actionLoginFragmentToMovieListFragment(user)
            )
        } else {
            showSnackBarInt.value = R.string.error_login_display_name
        }
    }
}
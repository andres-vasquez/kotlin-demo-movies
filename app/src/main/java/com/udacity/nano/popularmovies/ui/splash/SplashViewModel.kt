package com.udacity.nano.popularmovies.ui.splash

import android.app.Application
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.ui.base.BaseViewModel
import com.udacity.nano.popularmovies.ui.base.NavigationCommand

class SplashViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

    fun navigateToTheNextScreen() {
        if (repository.getUserPrefs() != null) {
            navigationCommand.value =
                NavigationCommand.To(
                    SplashFragmentDirections.actionSplashFragmentToMovieListFragment(repository.getUserPrefs()!!)
                )
        } else {
            navigationCommand.value =
                NavigationCommand.To(
                    SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                )
        }
    }
}
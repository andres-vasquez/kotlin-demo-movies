package com.udacity.nano.popularmovies.ui.movielist

import android.app.Application
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.ui.base.BaseViewModel

class MovieListViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

}

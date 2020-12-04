package com.udacity.nano.popularmovies.ui.moviedetail

import android.app.Application
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.ui.base.BaseViewModel

class MovieDetailViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {
}
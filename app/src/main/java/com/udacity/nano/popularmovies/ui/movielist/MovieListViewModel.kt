package com.udacity.nano.popularmovies.ui.movielist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus
import com.udacity.nano.popularmovies.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MovieListViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

    val movies: LiveData<List<PopularMovie>> = repository.movies
    val status: LiveData<ApiStatus> = repository.status

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            repository.refreshData()
        }
    }
}

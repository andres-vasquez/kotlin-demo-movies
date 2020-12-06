package com.udacity.nano.popularmovies.ui.moviedetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.remote.model.Genre
import com.udacity.nano.popularmovies.ui.base.BaseViewModel
import com.udacity.nano.popularmovies.ui.base.NavigationCommand
import kotlinx.coroutines.launch

class MovieDetailViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>> get() = _genres

    fun loadGenres(movie: PopularMovie) {
        if (!movie.genre_ids.isNullOrEmpty()) {
            val set: Set<Int> = movie.genre_ids.toHashSet()
            viewModelScope.launch {
                _genres.postValue(repository.getGenres(set))
            }
        }
    }

    fun navigateToBack() {
        navigationCommand.value = NavigationCommand.Back
    }
}
package com.udacity.nano.popularmovies.ui.movielist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.source.local.toDomainModel
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus
import com.udacity.nano.popularmovies.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieListViewModel(val app: Application, private val repository: MovieRepositoryI) :
    BaseViewModel(app, repository) {

    val status: LiveData<ApiStatus> = repository.status
    private var currentSearchResult: Flow<PagingData<PopularMovie>>? = null

    fun loadMovies(): Flow<PagingData<PopularMovie>> {
        val lastResult = currentSearchResult
        if (lastResult != null) {
            return lastResult
        }

        val newResult: Flow<PagingData<PopularMovie>> = repository.getPagingMovies()
            .map { pagingData -> pagingData.map { it.toDomainModel() } }
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}

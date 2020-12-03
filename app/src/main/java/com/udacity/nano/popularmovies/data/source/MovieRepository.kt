package com.udacity.nano.popularmovies.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus
import com.udacity.nano.popularmovies.data.source.remote.MoviesApi
import com.udacity.nano.popularmovies.data.source.remote.RemoteDataSourceI
import com.udacity.nano.popularmovies.data.source.remote.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.map
import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.local.*
import com.udacity.nano.popularmovies.data.succeeded
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception

class MovieRepository(
    private val local: LocalDataSourceI,
    private val remote: RemoteDataSourceI
) {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    val movies: LiveData<List<PopularMovie>> =
        Transformations.map(local.observeMovies()) {
            if (it != null && it is Result.Success) {
                it.data.asDomainModel()
            } else {
                ArrayList<PopularMovie>()
            }
        }

    suspend fun refreshData() {
        if (_status.value != ApiStatus.LOADING) {
            withContext(Dispatchers.IO) {
                try {
                    updateStatus(ApiStatus.LOADING)
                    val results: Result<List<PopularMovie>> = remote.getPopularMovies()
                    if (results is Result.Success) {
                        val dbMovies = results.data.asDatabaseModel()
                        local.deleteMovies()
                        local.insertMovies(*dbMovies)
                    } else {
                        if (results is Result.Error) {
                            Timber.e(results.exception)
                        } else {
                            Timber.e(results.toString())
                        }
                        updateStatus(ApiStatus.ERROR)
                    }
                    updateStatus(ApiStatus.DONE)
                } catch (e: Exception) {
                    updateStatus(ApiStatus.ERROR)
                }
            }
        }
    }

    private suspend fun updateStatus(status: ApiStatus) {
        withContext(Dispatchers.Main) {
            _status.value = status
        }
    }

    fun cleanStatus() {
        _status.value = ApiStatus.DONE
    }
}
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
import com.udacity.nano.popularmovies.data.source.prefs.PrefsDataSourceI
import com.udacity.nano.popularmovies.data.succeeded
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception

class MovieRepository(
    private val prefs: PrefsDataSourceI,
    private val local: LocalDataSourceI,
    private val remote: RemoteDataSourceI
) : MovieRepositoryI {

    private val _status = MutableLiveData<ApiStatus>()
    override val status: LiveData<ApiStatus>
        get() = _status

    override val movies: LiveData<List<PopularMovie>> =
        Transformations.map(local.observeMovies()) {
            if (it != null && it is Result.Success) {
                it.data.asDomainModel()
            } else {
                ArrayList<PopularMovie>()
            }
        }

    override suspend fun refreshData() {
        if (_status.value != ApiStatus.LOADING) {
            withContext(Dispatchers.IO) {
                try {
                    updateStatus(ApiStatus.LOADING)
                    val lang = prefs.getLanguage()
                    val results: Result<List<PopularMovie>> = remote.getPopularMovies(lang)
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

    override fun getUserPrefs(): User? {
        return prefs.getUserPrefs()
    }

    override fun saveUserPrefs(user: User) {
        prefs.saveUserPrefs(user)
    }
}
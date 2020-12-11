package com.udacity.nano.popularmovies.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.local.LocalDataSourceI
import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.source.local.asDatabaseModel
import com.udacity.nano.popularmovies.data.source.prefs.PrefsDataSourceI
import com.udacity.nano.popularmovies.data.source.remote.ApiStatus
import com.udacity.nano.popularmovies.data.source.remote.RemoteDataSourceI
import com.udacity.nano.popularmovies.data.source.remote.model.Genre
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlinx.coroutines.flow.Flow

const val STARTING_PAGE_INDEX = 1

@ExperimentalCoroutinesApi
class MovieRepository(
    private val prefs: PrefsDataSourceI,
    private val local: LocalDataSourceI,
    private val remote: RemoteDataSourceI
) : MovieRepositoryI {

    private val _status = MutableLiveData<ApiStatus>()
    override val status: LiveData<ApiStatus>
        get() = _status



    override fun getPagingMovies(): Flow<PagingData<MovieDTO>> {
        val pagingSourceFactory = { local.getMovies() }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(
                prefs,
                local,
                remote
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun refreshData() {
        if (_status.value != ApiStatus.LOADING) {
            withContext(Dispatchers.IO) {
                try {
                    updateStatus(ApiStatus.LOADING)
                    val lang = prefs.getLanguage()
                    val results: Result<List<PopularMovie>> =
                        remote.getPopularMovies(lang, STARTING_PAGE_INDEX)
                    if (results is Result.Success) {
                        val dbMovies = results.data.asDatabaseModel()
                        local.deleteMovies()
                        local.insertMovies(dbMovies.toList())
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

    override suspend fun getGenres(genres: Set<Int>): List<Genre> {
        return try {
            val lang = prefs.getLanguage()
            val results: Result<List<Genre>> = remote.getGenres(lang)
            if (results is Result.Success) {
                return results.data.filter { it -> genres.contains(it.id) }
            } else {
                if (results is Result.Error) {
                    Timber.e(results.exception)
                } else {
                    Timber.e(results.toString())
                }
                updateStatus(ApiStatus.ERROR)
                listOf()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            listOf()
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}
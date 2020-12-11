package com.udacity.nano.popularmovies.data.source

import androidx.paging.*
import com.example.android.codelabs.paging.db.RemoteKeys
import com.udacity.nano.popularmovies.data.Result
import com.udacity.nano.popularmovies.data.source.local.LocalDataSourceI
import com.udacity.nano.popularmovies.data.source.local.MovieDTO
import com.udacity.nano.popularmovies.data.source.local.asDatabaseModel
import com.udacity.nano.popularmovies.data.source.prefs.PrefsDataSourceI
import com.udacity.nano.popularmovies.data.source.remote.RemoteDataSourceI
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import java.lang.NullPointerException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val prefs: PrefsDataSourceI,
    private val local: LocalDataSourceI,
    private val remote: RemoteDataSourceI
) : RemoteMediator<Int, MovieDTO>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDTO>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        try {
            val lang = prefs.getLanguage()
            val apiResponse = remote.getPopularMovies(lang, page)

            if (apiResponse is Result.Success) {
                val movies = apiResponse.data
                val endOfPaginationReached = movies.isEmpty()

                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    local.clearRemoteKeys()
                    local.deleteMovies()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    RemoteKeys(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                local.insertAllRemoteKeys(keys)
                local.insertMovies(movies.asDatabaseModel().toList())

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else if (apiResponse is Result.Error) {
                return MediatorResult.Error(apiResponse.exception)
            } else {
                return MediatorResult.Error(NullPointerException())
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieDTO>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                local.remoteKeysMovieId(movieId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieDTO>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                // Get the remote keys of the first items retrieved
                local.remoteKeysMovieId(movie.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieDTO>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
                local.remoteKeysMovieId(movie.id)
            }
    }
}
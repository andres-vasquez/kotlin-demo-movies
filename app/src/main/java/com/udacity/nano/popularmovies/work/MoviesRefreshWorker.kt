package com.udacity.nano.popularmovies.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.nano.popularmovies.data.source.MovieRepository
import com.udacity.nano.popularmovies.data.source.local.LocalDataSource
import com.udacity.nano.popularmovies.data.source.local.getDatabase
import com.udacity.nano.popularmovies.data.source.remote.MoviesApi
import com.udacity.nano.popularmovies.data.source.remote.RemoteDataSource
import retrofit2.HttpException
import java.lang.Exception

class MoviesRefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "MoviesRefreshWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val local = LocalDataSource(database)
        val remote = RemoteDataSource()
        val repository = MovieRepository(local, remote)

        return try {
            repository.refreshData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

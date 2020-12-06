package com.udacity.nano.popularmovies.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import org.koin.core.KoinComponent
import org.koin.core.inject

class MoviesRefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params), KoinComponent {

    companion object {
        const val WORK_NAME = "MoviesRefreshWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val repository: MovieRepositoryI by inject()
            repository.refreshData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

package com.udacity.nano.popularmovies

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.nano.popularmovies.work.MoviesRefreshWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MoviesApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        updateMovies()
    }

    private fun updateMovies() {
        applicationScope.launch {
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()


            val repeatingRequest = PeriodicWorkRequestBuilder<MoviesRefreshWorker>(
                1,
                TimeUnit.DAYS
            )
                .setConstraints(constrains)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                MoviesRefreshWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
        }
    }
}
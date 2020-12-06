package com.udacity.nano.popularmovies

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.nano.popularmovies.data.source.MovieRepository
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.data.source.local.LocalDataSource
import com.udacity.nano.popularmovies.data.source.local.LocalDataSourceI
import com.udacity.nano.popularmovies.data.source.local.getDatabase
import com.udacity.nano.popularmovies.data.source.prefs.PrefsDataSource
import com.udacity.nano.popularmovies.data.source.prefs.PrefsDataSourceI
import com.udacity.nano.popularmovies.data.source.remote.RemoteDataSource
import com.udacity.nano.popularmovies.data.source.remote.RemoteDataSourceI
import com.udacity.nano.popularmovies.ui.login.LoginViewModel
import com.udacity.nano.popularmovies.ui.moviedetail.MovieDetailViewModel
import com.udacity.nano.popularmovies.ui.movielist.MovieListViewModel
import com.udacity.nano.popularmovies.ui.splash.SplashViewModel
import com.udacity.nano.popularmovies.work.MoviesRefreshWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MoviesApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        initDependencyInjection()
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

    fun initDependencyInjection() {
        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel { SplashViewModel(get(), get()) }
            viewModel { LoginViewModel(get(), get()) }
            viewModel { MovieListViewModel(get(), get()) }
            viewModel { MovieDetailViewModel(get(), get()) }
            single {
                MovieRepository(get(), get(), get()) as MovieRepositoryI
            }
            single {
                LocalDataSource(get()) as LocalDataSourceI
            }
            single {
                RemoteDataSource() as RemoteDataSourceI
            }
            single {
                PrefsDataSource(this@MoviesApp) as PrefsDataSourceI
            }
            single {
                getDatabase(this@MoviesApp)
            }
        }

        startKoin {
            androidContext(this@MoviesApp)
            modules(listOf(myModule))
        }
    }
}
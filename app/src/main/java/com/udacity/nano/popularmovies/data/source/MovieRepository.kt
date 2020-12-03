package com.udacity.nano.popularmovies.data.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {

    suspend fun refreshData() {

    }

    suspend fun deleteAllRecords() {
        withContext(Dispatchers.IO) {

        }
    }

}
package com.udacity.nano.popularmovies.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieDTO::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDAO
}
package com.udacity.nano.popularmovies.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.udacity.nano.popularmovies.utils.DatabaseListTypeConverter
import com.udacity.nano.popularmovies.utils.DatabaseTypeConverter

@Database(entities = [MovieDTO::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverter::class, DatabaseListTypeConverter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDAO
}

private lateinit var INSTANCE: MoviesDatabase
fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviesDatabase::class.java,
                "movies_db"
            ).build()
        }
    }
    return INSTANCE
}
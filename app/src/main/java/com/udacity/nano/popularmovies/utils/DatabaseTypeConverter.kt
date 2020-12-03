package com.udacity.nano.popularmovies.utils

import androidx.room.TypeConverter
import java.util.*

class DatabaseTypeConverter {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromLong(long: Long): Date {
        return Date(long)
    }
}
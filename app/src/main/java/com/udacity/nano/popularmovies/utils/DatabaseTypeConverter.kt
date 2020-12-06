package com.udacity.nano.popularmovies.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
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

class DatabaseListTypeConverter {
    @TypeConverter
    fun fromList(data: List<Int>): String {
        return Gson().toJson(data)
    }

    @TypeConverter
    fun toList(data: String): List<Int> {
        val listType: Type = object : TypeToken<ArrayList<Int?>?>() {}.getType()
        return Gson().fromJson(data, listType)
    }
}
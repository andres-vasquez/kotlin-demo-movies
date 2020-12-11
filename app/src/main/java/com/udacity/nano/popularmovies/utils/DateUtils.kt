package com.udacity.nano.popularmovies.utils

import java.text.SimpleDateFormat
import java.util.*

fun dateToString(date: Date?, format: String = Constants.DATE_FORMAT): String {
    return if (date != null) {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        dateFormat.format(date)
    } else {
        ""
    }
}

fun strToDate(dateAsString: String): Date? {
    return try {
        SimpleDateFormat(Constants.DATE_FORMAT).parse(dateAsString)
    } catch (ex: Exception) {
        null
    }
}
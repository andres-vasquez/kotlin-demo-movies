package com.udacity.nano.popularmovies.utils

import java.text.SimpleDateFormat
import java.util.*

fun dateToString(date: Date): String {
    val dateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(date)
}

fun strToDate(dateAsString: String): Date {
    return SimpleDateFormat(Constants.DATE_FORMAT).parse(dateAsString);
}
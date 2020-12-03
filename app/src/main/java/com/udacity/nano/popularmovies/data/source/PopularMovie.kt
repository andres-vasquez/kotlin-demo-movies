package com.udacity.nano.popularmovies.data.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PopularMovie(
    val id: Long,
    val displayName: String,
    val language: String,
    val overview: String,
    val adult: Boolean,
    val popularity: Double,
    val image: String,
    val releaseDate: String
) : Parcelable
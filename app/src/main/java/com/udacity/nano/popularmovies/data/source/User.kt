package com.udacity.nano.popularmovies.data.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val displayName: String,
    val photo: String?,
    val language: String?,
) : Parcelable
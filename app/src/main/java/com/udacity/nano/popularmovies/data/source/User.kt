package com.udacity.nano.popularmovies.data.source

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var displayName: String,
    var photo: String?,
    var language: String?,
) : Parcelable
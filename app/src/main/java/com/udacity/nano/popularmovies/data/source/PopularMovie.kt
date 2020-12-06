package com.udacity.nano.popularmovies.data.source

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PopularMovie(
    /* Moshi is not working with this API
    val id: Long,
    @Json(name = "original_title") val displayName: String,
    @Json(name = "original_language") val language: String,
    val overview: String,
    val adult: Boolean,
    val popularity: Double,
    @Json(name = "poster_path") val image: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val rating: Double */

    val id: Long,
    @SerializedName("original_title") val displayName: String,
    @SerializedName("original_language") val language: String,
    val overview: String,
    val adult: Boolean,
    val popularity: Double,
    @SerializedName("poster_path") val image: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("vote_average") val rating: Double,
    @SerializedName("genre_ids") val genre_ids: List<Int>
) : Parcelable
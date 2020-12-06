package com.udacity.nano.popularmovies.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.utils.Constants
import com.udacity.nano.popularmovies.utils.dateToString
import com.udacity.nano.popularmovies.utils.strToDate
import java.util.*

@Entity(tableName = "movie")
data class MovieDTO @JvmOverloads constructor(
    @ColumnInfo(name = "id") @PrimaryKey var id: Long,
    @ColumnInfo(name = "original_title") val original_title: String,
    @ColumnInfo(name = "original_language") val original_language: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "adult") val adult: Boolean,
    @ColumnInfo(name = "popularity") val popularity: Double,
    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "release_date") val release_date: Date,
    @ColumnInfo(name = "rating") val rating: Double,
    @SerializedName("genre_ids") val genre_ids: List<Int>
)

fun List<MovieDTO>.asDomainModel(): List<PopularMovie> {
    return map {
        PopularMovie(
            id = it.id,
            displayName = it.original_title,
            overview = it.overview,
            language = it.original_language,
            adult = it.adult,
            image = it.poster_path,
            popularity = it.popularity,
            releaseDate = dateToString(it.release_date, Constants.DATE_FORMAT_DISPLAY),
            rating = it.rating,
            genre_ids = it.genre_ids
        )
    }
}

fun List<PopularMovie>.asDatabaseModel(): Array<MovieDTO> {
    return map {
        MovieDTO(
            id = it.id,
            original_title = it.displayName,
            original_language = it.language,
            overview = it.overview,
            adult = it.adult,
            popularity = it.popularity,
            poster_path = it.image,
            release_date = strToDate(it.releaseDate),
            rating = it.rating,
            genre_ids = it.genre_ids
        )
    }.toTypedArray()
}
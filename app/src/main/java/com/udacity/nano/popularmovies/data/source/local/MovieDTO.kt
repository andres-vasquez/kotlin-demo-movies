package com.udacity.nano.popularmovies.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieDTO @JvmOverloads constructor(
    @ColumnInfo(name = "id") @PrimaryKey var id: Int,
    @ColumnInfo(name = "original_title") val original_title: String,
    @ColumnInfo(name = "original_language") val original_language: String,
    @ColumnInfo(name = "overview") val overview: String,
    @ColumnInfo(name = "adult") val adult: Boolean,
    @ColumnInfo(name = "popularity") val popularity: Double,
    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "release_date") val release_date: String
) {

}
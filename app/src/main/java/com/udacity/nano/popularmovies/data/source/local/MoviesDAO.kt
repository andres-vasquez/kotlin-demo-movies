package com.udacity.nano.popularmovies.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDAO {
    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getMovies(): PagingSource<Int, MovieDTO>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieDTO>)

    @Query("DELETE FROM movie")
    suspend fun deleteMovies()
}
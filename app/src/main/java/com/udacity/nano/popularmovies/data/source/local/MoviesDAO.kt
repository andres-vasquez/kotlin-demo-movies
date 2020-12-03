package com.udacity.nano.popularmovies.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDAO {
    @Query("SELECT * FROM movie")
    fun observeMovies(): LiveData<List<MovieDTO>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieDTO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movies: MovieDTO)

    @Query("DELETE FROM movie")
    suspend fun deleteMovies()
}
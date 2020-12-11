package com.udacity.nano.popularmovies.data.source.remote

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.nano.popularmovies.data.source.remote.model.GenresResponse
import com.udacity.nano.popularmovies.data.source.remote.model.MoviesResponse
import com.udacity.nano.popularmovies.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class ApiStatus { LOADING, ERROR, DONE }

interface MoviesService {
    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language") lang: String,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("/3/genre/movie/list")
    suspend fun getGenres(@Query("language") lang: String): GenresResponse
}

private var loggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BASIC)

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor { chain ->
        val original: Request = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("api_key", Constants.API_KEY)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        return@addInterceptor chain.proceed(request)
    }.build()

val gson = GsonBuilder()
    .setLenient()
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .client(httpClient)
    .build()


object MoviesApi {
    val retrofitService: MoviesService by lazy { retrofit.create(MoviesService::class.java) }
}
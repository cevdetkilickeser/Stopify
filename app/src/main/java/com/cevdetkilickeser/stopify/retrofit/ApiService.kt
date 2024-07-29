package com.cevdetkilickeser.stopify.retrofit

import com.cevdetkilickeser.stopify.data.genre.GenreResponse
import retrofit2.http.GET

interface ApiService {

    @GET("genre")
    suspend fun getGenreResponse(): GenreResponse
}
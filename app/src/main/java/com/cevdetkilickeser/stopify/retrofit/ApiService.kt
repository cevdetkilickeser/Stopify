package com.cevdetkilickeser.stopify.retrofit

import com.cevdetkilickeser.stopify.data.genre.GenreResponse
import com.cevdetkilickeser.stopify.data.playlist.PlaylistResponse
import com.cevdetkilickeser.stopify.data.single_genre.SingleGenreResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("genre")
    suspend fun getGenreResponse(): GenreResponse

    @GET("genre/{id}/radios")
    suspend fun getSingleGenreResponse(@Path("id") id: String): SingleGenreResponse

    @GET("radio/{id}/tracks")
    suspend fun getPlaylistResponse(@Path("id") id: String): PlaylistResponse
}
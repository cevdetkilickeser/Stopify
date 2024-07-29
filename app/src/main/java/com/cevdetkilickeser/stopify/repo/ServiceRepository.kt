package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.genre.GenreData
import com.cevdetkilickeser.stopify.data.playlist.SingleGenreData
import com.cevdetkilickeser.stopify.retrofit.ApiService

class ServiceRepository(private val apiService: ApiService) {

    suspend fun getGenreDataList(): List<GenreData> =
        apiService.getGenreResponse().genreDataList

    suspend fun getSingleGenreDataList(genreId: String): List<SingleGenreData> =
        apiService.getSingleGenreResponse(genreId).singleGenreData
}
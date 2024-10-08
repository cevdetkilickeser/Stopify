package com.cevdetkilickeser.stopify.retrofit

import com.cevdetkilickeser.stopify.data.model.album.AlbumResponse
import com.cevdetkilickeser.stopify.data.model.artist.ArtistAlbum
import com.cevdetkilickeser.stopify.data.model.artist.ArtistResponse
import com.cevdetkilickeser.stopify.data.model.genre.GenreResponse
import com.cevdetkilickeser.stopify.data.model.playlist.PlaylistResponse
import com.cevdetkilickeser.stopify.data.model.search.SearchByAlbumResponse
import com.cevdetkilickeser.stopify.data.model.search.SearchByArtistResponse
import com.cevdetkilickeser.stopify.data.model.search.SearchResponse
import com.cevdetkilickeser.stopify.data.model.single_genre.SingleGenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("genre")
    suspend fun getGenreResponse(): GenreResponse

    @GET("genre/{id}/radios")
    suspend fun getSingleGenreResponse(@Path("id") id: String): SingleGenreResponse

    @GET("radio/{id}/tracks")
    suspend fun getPlaylistResponse(@Path("id") id: String): PlaylistResponse

    @GET("search")
    suspend fun getSearchResponse(@Query("q") q: String): SearchResponse

    @GET("search/artist")
    suspend fun getSearchByArtistResponse(@Query("q") q: String): SearchByArtistResponse

    @GET("search/album")
    suspend fun getSearchByAlbumResponse(@Query("q") q: String): SearchByAlbumResponse

    @GET("artist/{id}/albums")
    suspend fun getArtistAlbum(@Path("id") id: String): ArtistAlbum

    @GET("artist/{id}")
    suspend fun getArtistResponse(@Path("id") id: String): ArtistResponse

    @GET("album/{id}")
    suspend fun getAlbumResponse(@Path("id") id: String): AlbumResponse

}
package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.album.AlbumResponse
import com.cevdetkilickeser.stopify.data.artist.ArtistAlbumData
import com.cevdetkilickeser.stopify.data.artist.ArtistResponse
import com.cevdetkilickeser.stopify.data.genre.GenreData
import com.cevdetkilickeser.stopify.data.playlist.Track
import com.cevdetkilickeser.stopify.data.search.AlbumData
import com.cevdetkilickeser.stopify.data.search.ArtistData
import com.cevdetkilickeser.stopify.data.single_genre.SingleGenreData
import com.cevdetkilickeser.stopify.retrofit.ApiService

class ServiceRepository(private val apiService: ApiService) {

    suspend fun getGenreDataList(): List<GenreData> =
        apiService.getGenreResponse().genreDataList

    suspend fun getSingleGenreDataList(genreId: String): List<SingleGenreData> =
        apiService.getSingleGenreResponse(genreId).singleGenreDataList

    suspend fun getTrackList(playlistId: String): List<Track> =
        apiService.getPlaylistResponse(playlistId).trackList

    suspend fun getSearchResponse(query: String): List<Track> =
        apiService.getSearchResponse(query).trackList

    suspend fun getSearchByArtistResponse(query: String): List<ArtistData> =
        apiService.getSearchByArtistResponse(query).artistDataList

    suspend fun getSearchByAlbumResponse(query: String): List<AlbumData> =
        apiService.getSearchByAlbumResponse(query).albumDataList

    suspend fun getArtistAlbum(id: String): List<ArtistAlbumData> =
        apiService.getArtistAlbum(id).artistAlbumDataList

    suspend fun getArtistResponse(artistId: String): ArtistResponse =
        apiService.getArtistResponse(artistId)

    suspend fun getAlbumResponse(alumId: String): AlbumResponse =
        apiService.getAlbumResponse(alumId)
}
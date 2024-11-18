package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.model.album.AlbumResponse
import com.cevdetkilickeser.stopify.data.model.artist.ArtistAlbumData
import com.cevdetkilickeser.stopify.data.model.artist.ArtistResponse
import com.cevdetkilickeser.stopify.data.model.genre.GenreData
import com.cevdetkilickeser.stopify.data.model.playlist.Track
import com.cevdetkilickeser.stopify.data.model.search.AlbumData
import com.cevdetkilickeser.stopify.data.model.search.ArtistData
import com.cevdetkilickeser.stopify.data.model.single_genre.SingleGenreData
import com.cevdetkilickeser.stopify.retrofit.ApiService
import javax.inject.Inject

class ServiceRepository @Inject constructor(private val apiService: ApiService) {

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
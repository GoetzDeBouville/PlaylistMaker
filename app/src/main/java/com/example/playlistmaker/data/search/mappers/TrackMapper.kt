package com.example.playlistmaker.data.search.mappers

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.search.models.Track
import com.google.gson.Gson

class TrackMapper {
    fun createJsonFromTracksList(tracks: Array<Track>): String {
        return Gson().toJson(tracks)
    }

    fun createTracksListFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    fun mapDtoToEntity(dto: TrackDto, isFavorite: Boolean) = Track(
        trackId = dto.trackId,
        trackName = dto.trackName,
        artistName = dto.artistName,
        trackTimeMillis = dto.trackTimeMillis,
        artworkUrl100 = dto.artworkUrl100,
        collectionName = dto.collectionName,
        releaseDate = dto.releaseDate,
        primaryGenreName = dto.primaryGenreName,
        country = dto.country,
        previewUrl = dto.previewUrl,
        isFavorite = isFavorite
    )
}

package com.example.playlistmaker.data.converters

import com.example.playlistmaker.db.entity.SavedTrackEntity
import com.example.playlistmaker.domain.search.models.Track

class SavedTrackDbConverter {
    fun map(track: SavedTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: Track): SavedTrackEntity {
        return SavedTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            timeStamp = System.currentTimeMillis()
        )
    }
}
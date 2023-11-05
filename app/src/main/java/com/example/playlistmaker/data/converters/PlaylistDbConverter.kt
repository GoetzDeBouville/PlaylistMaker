package com.example.playlistmaker.data.converters

import androidx.core.net.toUri
import com.example.playlistmaker.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.media.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            imagePath = playlist.imagePath.toString(),
            trackAmount = playlist.trackAmount
        )
    }

    fun map(playlist: PlaylistEntity) : Playlist {
        return Playlist(
            id = playlist.id,
            title = playlist.title!!,
            description = playlist.description,
            imagePath = playlist.imagePath?.toUri(),
            trackAmount = playlist.trackAmount
        )
    }
}
package com.example.playlistmaker.domain.media.models


sealed interface PlaylistState {
    object Empty : PlaylistState
    data class Content(val playlists: List<Playlist>) : PlaylistState

}
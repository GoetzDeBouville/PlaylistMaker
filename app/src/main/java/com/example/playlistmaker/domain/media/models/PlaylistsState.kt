package com.example.playlistmaker.domain.media.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface PlaylistsState  {
    object Empty : PlaylistsState

    data class Content(val playlists: List<Playlist>) : PlaylistsState
}

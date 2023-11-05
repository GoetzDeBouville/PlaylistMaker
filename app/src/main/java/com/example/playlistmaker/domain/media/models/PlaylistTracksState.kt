package com.example.playlistmaker.domain.media.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface PlaylistTracksState {
    object Empty : PlaylistTracksState
    data class Content(val trackList: List<Track>) : PlaylistTracksState
}
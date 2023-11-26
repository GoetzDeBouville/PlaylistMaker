package com.example.playlistmaker.domain.media.models

import com.example.playlistmaker.domain.search.models.Track

sealed interface FavoriteTracksState  {
    object Empty : FavoriteTracksState

    data class Content(val favoriteTracks: List<Track>) : FavoriteTracksState
}

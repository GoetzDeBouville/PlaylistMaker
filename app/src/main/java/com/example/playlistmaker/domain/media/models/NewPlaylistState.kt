package com.example.playlistmaker.domain.media.models

sealed interface NewPlaylistState {
    object Empty : NewPlaylistState

    object NotEmpty : NewPlaylistState
}

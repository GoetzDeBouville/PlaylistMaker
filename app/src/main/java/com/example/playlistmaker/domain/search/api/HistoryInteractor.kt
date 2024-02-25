package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface HistoryInteractor {
    fun clearHistory()

    fun getAllTracks(): List<Track>

    fun saveTrack(track: Track)
}

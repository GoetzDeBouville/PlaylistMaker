package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track

interface HistoryInteractor {
    fun saveTrack(track: Track)

    fun getAllTracks(): List<Track>

    fun clearHistory()
}

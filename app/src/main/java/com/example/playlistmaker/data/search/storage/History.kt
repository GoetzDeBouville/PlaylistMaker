package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.domain.search.models.Track

interface History {
    fun saveTrack(track: Track)

    fun getAllTracks(): List<Track>

    fun clearHistory()
}
package com.example.playlistmaker.data.search.storage

import com.example.playlistmaker.domain.search.models.Track

interface History {
    fun clearHistory()
    fun getAllTracks(): List<Track>
    fun saveTrack(track: Track)
}

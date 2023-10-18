package com.example.playlistmaker.data.search.repository

import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.domain.search.api.HistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class HistoryRepositoryImpl(private val storage: History) : HistoryRepository {
    override fun clearHistory() {
        storage.clearHistory()
    }

    override fun getAllTracks(): List<Track> {
        return storage.getAllTracks()
    }

    override fun saveTrack(track: Track) {
        storage.saveTrack(track)
    }
}

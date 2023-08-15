package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.api.HistoryInteractor
import com.example.playlistmaker.domain.search.api.HistoryRepository
import com.example.playlistmaker.domain.search.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository): HistoryInteractor {
    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun getAllTracks(): List<Track> {
        return repository.getAllTracks()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}
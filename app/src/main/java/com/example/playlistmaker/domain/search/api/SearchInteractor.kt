package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.LoadingStatus

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun getConnectionErrorMessage() : String
    fun getEmptyListMessage() : String
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorType: LoadingStatus?)
    }
}
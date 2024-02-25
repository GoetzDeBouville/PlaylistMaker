package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.LoadingStatus
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun searchTracks(expression: String): Flow<Pair<List<Track>?, LoadingStatus?>>
}

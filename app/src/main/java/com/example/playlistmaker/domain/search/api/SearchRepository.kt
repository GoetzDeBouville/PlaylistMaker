package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>
}

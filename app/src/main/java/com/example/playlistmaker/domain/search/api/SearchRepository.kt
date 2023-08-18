package com.example.playlistmaker.domain.search.api

import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.Resource

interface SearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}

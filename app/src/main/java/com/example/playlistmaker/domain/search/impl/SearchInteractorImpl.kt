package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.LoadingStatus
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val repository: SearchRepository
) : SearchInteractor {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, LoadingStatus?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.errorType)
            }
        }
    }
}

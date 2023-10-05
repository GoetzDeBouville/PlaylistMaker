package com.example.playlistmaker.domain.search.models

sealed interface SearchState {
    data class SearchHistory(
        val trackList: List<Track>
    ) : SearchState
    data class Content(
        val trackList: List<Track>
    ) : SearchState
    object Loading : SearchState
    object Empty : SearchState
    object ConnectionError : SearchState
}

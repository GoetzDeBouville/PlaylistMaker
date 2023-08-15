package com.example.playlistmaker.domain.search.models

sealed interface SearchActivityState {
    data class SearchHistory(
        val trackList: List<Track>
    ) : SearchActivityState
    data class Content(
        val trackList: List<Track>
    ) : SearchActivityState
    object Loading : SearchActivityState
    data class Empty(val emptyMessage: String) : SearchActivityState
    data class Error(val errorMessage: String) : SearchActivityState
}
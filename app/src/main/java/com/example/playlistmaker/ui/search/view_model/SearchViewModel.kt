package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.api.HistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.models.SearchState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.LoadingStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    private var latestSearchText: String = ""
    private var isClickAllowed = true
    private var searchJob: Job? = null

    private val _state = MutableLiveData<SearchState>()
    var savedSearchRequest = ""
    val state: LiveData<SearchState>
        get() = _state

    fun searchDebounce(searchText: String) {
        if (searchText.isBlank()) {
            _state.value = SearchState.SearchHistory(getHistory())
        } else {
            this.latestSearchText = searchText

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchRequest(searchText)
            }
        }
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                searchInteractor.searchTracks(newSearchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorType: LoadingStatus?) {
        val tracks = mutableListOf<Track>()

        if (foundTracks != null) tracks.addAll(foundTracks)

        when {
            errorType != null -> renderState(SearchState.ConnectionError)
            tracks.isEmpty() -> renderState(SearchState.Empty)
            else -> renderState(SearchState.Content(trackList = tracks))
        }
    }

    private fun renderState(state: SearchState) {
        _state.postValue(state)
    }

    private fun getHistory() = historyInteractor.getAllTracks()

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun saveTrack(track: Track) {
        historyInteractor.saveTrack(track)
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        _state.value = SearchState.SearchHistory(
            emptyList()
        )
    }

    fun showHistory() {
        _state.value = SearchState.SearchHistory(
            getHistory()
        )
    }

    fun stopSearch() {
        showHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}

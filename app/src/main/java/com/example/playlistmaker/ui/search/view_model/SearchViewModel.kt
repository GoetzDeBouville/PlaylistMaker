package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.search.api.HistoryInteractor
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.models.SearchActivityState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.LoadingStatus
class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel(){

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest(latestSearchText) }

    private var latestSearchText: String = ""
    var savedSearchRequest: String = ""

    private var isClickAllowed = true

    private val _state = MutableLiveData<SearchActivityState>()
    val state: LiveData<SearchActivityState>
        get() = _state

    fun searchDebounce(searchText: String) { //* FIN
        if (searchText.isBlank()) {
            _state.value = SearchActivityState.SearchHistory(getHistory())
        } else {
            this.latestSearchText = searchText
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchActivityState.Loading)

            searchInteractor.searchTracks(newSearchText, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorType: LoadingStatus?) {
                    Log.e("SearchViewModel", "foundTracks: $foundTracks, errorType: $errorType")
                    val tracks = mutableListOf<Track>()
                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)
                    }

                    when {
                        errorType != null -> {
                            renderState(
                                SearchActivityState.Error(
                                    errorMessage = searchInteractor.getConnectionErrorMessage()
                                )
                            )
                        }

                        tracks.isEmpty() -> {
                            renderState(
                                SearchActivityState.Empty(
                                    emptyMessage = searchInteractor.getEmptyListMessage()
                                )
                            )
                        }

                        else -> {
                            renderState(
                                SearchActivityState.Content(
                                    trackList = tracks
                                )
                            )
                        }
                    }

                }
            })
        }
    }

    private fun renderState(state: SearchActivityState) {
        Log.e("SearchViewModel", "Render state: $state")
        _state.postValue(state)
    }
    private fun getHistory() = historyInteractor.getAllTracks()

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    fun saveTrack(track: Track) {
        historyInteractor.saveTrack(track)
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        _state.value = SearchActivityState.SearchHistory(
            emptyList()
        )
    }

    fun showHistory() {
        _state.value = SearchActivityState.SearchHistory(
            getHistory()
        )
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
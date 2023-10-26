package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.PlaylistsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistsViewModel (private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<PlaylistsState>()
    val state: LiveData<PlaylistsState> = _state
    private var isClickAllowed = true

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(PlaylistsViewModel.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                if (it.isEmpty()) _state.postValue(PlaylistsState.Empty)
                else _state.postValue(PlaylistsState.Content(it))
            }
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}

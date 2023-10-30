package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.NewPlaylistState
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<NewPlaylistState>(NewPlaylistState.Empty)
    val state: LiveData<NewPlaylistState> = _state

    private var isClickAllowed = true

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(Tools.CLICK_DEBOUNCE_DELAY_MS)
                isClickAllowed = true
            }
        }
        return current
    }
    fun savePlayList(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addNewPlaylist(playlist)
        }
    }

    fun setEmptyState() {
        _state.postValue(NewPlaylistState.Empty)
    }

    fun setNotEmptyState() {
        _state.postValue(NewPlaylistState.NotEmpty)
    }
}
package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistsViewModel (private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<PlaylistState>()
    val state: LiveData<PlaylistState> = _state
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

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect {
                if (it.isEmpty()) _state.postValue(PlaylistState.Empty)
                else _state.postValue(PlaylistState.Content(it))
            }
        }
    }
}

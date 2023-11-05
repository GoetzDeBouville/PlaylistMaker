package com.example.playlistmaker.ui.singleplaylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.domain.media.models.PlaylistTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SinglePlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val _playlistState = MutableLiveData<PlaylistTracksState>()
    val playlistState : LiveData<PlaylistTracksState>
        get() = _playlistState
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

    fun getTracks(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.getTracks(playlistId).collect {
                if (it.isEmpty()) _playlistState.postValue(PlaylistTracksState.Empty)
                else _playlistState.postValue(PlaylistTracksState.Content(it))
            }
        }
    }
}
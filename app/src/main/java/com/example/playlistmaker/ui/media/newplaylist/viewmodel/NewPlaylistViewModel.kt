package com.example.playlistmaker.ui.media.newplaylist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.NewPlaylistState
import com.example.playlistmaker.domain.media.models.Playlist
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val _state = MutableLiveData<NewPlaylistState>(NewPlaylistState.Empty)
    open val state: LiveData<NewPlaylistState> = _state
    open fun savePlayList(playlist: Playlist) {
        viewModelScope.launch {
            try {
                playlistInteractor.addNewPlaylist(playlist)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    open fun setEmptyState() {
        _state.postValue(NewPlaylistState.Empty)
    }

    open fun setNotEmptyState() {
        _state.postValue(NewPlaylistState.NotEmpty)
    }
}
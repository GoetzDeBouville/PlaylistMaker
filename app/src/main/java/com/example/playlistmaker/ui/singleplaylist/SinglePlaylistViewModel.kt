package com.example.playlistmaker.ui.singleplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.PlaylistTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SinglePlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {
    private val _playlistState = MutableLiveData<PlaylistTracksState>()
    val playlistState : LiveData<PlaylistTracksState>
        get() = _playlistState
    private val _playlistDuration = MutableLiveData<String>()
    val playlistDuration: LiveData<String>
        get() = _playlistDuration

    private val _tracksNumber = MutableLiveData<String>()
    val tracksNumber : LiveData<String>
        get() = _tracksNumber

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
                else {
                    _playlistState.postValue(PlaylistTracksState.Content(it))
                }
            }
        }
    }

    fun calculatePlaylistDuration(tracks: List<Track>) {
        viewModelScope.launch {
            val duration = playlistInteractor.playlistDuration(tracks)
            _playlistDuration.postValue(duration)
        }
    }

    fun calculatetracksNumber(num : Int) {
        viewModelScope.launch {
            _tracksNumber.postValue(Tools.amountTextFormater(num))
        }
    }

    fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlistInteractor.removeSavedTrackFromPlaylist(playlist, track)
        }
    }
}
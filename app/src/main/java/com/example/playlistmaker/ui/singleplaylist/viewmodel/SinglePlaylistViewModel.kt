package com.example.playlistmaker.ui.singleplaylist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.PlaylistTracksState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.utils.Tools
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class SinglePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val _playlistState = MutableLiveData<PlaylistTracksState>()
    val playlistState: LiveData<PlaylistTracksState>
        get() = _playlistState
    private val _playlistDuration = MutableLiveData<String>()
    val playlistDuration: LiveData<String>
        get() = _playlistDuration

    private val _tracksNumber = MutableLiveData<String>()
    val tracksNumber: LiveData<String>
        get() = _tracksNumber

    private var isClickAllowed = true

    fun calculatePlaylistDuration(tracks: List<Track>) {
        viewModelScope.launch {
            try {
                val duration = playlistInteractor.playlistDuration(tracks)
                _playlistDuration.postValue(duration)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun calculateTracksNumber(num: Int) {
        viewModelScope.launch {
            try {
                _tracksNumber.postValue(Tools.amountTextFormater(num))
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                try {
                    delay(Tools.CLICK_DEBOUNCE_DELAY_MS)
                    isClickAllowed = true
                } catch (e: Exception) {
                    Log.e("Coroutine Exception", e.stackTraceToString())
                }
            }
        }
        return current
    }

    fun getTracks(playlistId: Int) {
        viewModelScope.launch {
            try {
                playlistInteractor.getTracks(playlistId).collect {
                    if (it.isEmpty()) _playlistState.postValue(PlaylistTracksState.Empty)
                    else _playlistState.postValue(PlaylistTracksState.Content(it))
                }
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun removePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                playlistInteractor.removePlaylist(playlist)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            try {
                playlistInteractor.removeSavedTrackFromPlaylist(playlist, track)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        var text =
            "${playlist.title} ${playlist.description}\n${Tools.amountTextFormater(tracks.size)}\n"
        val stringBuilder = StringBuilder()
        tracks.forEachIndexed { index, track ->
            stringBuilder.append("${index + 1}. ").append("${track.artistName} - ")
                .append("${track.trackName} ").append(
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                ).append("\n")
        }
        text += stringBuilder
        sharingInteractor.sharePlaylist(text)
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            try {
                playlistInteractor.updatePlaylist(playlist)
                getTracks(playlist.id)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }
}

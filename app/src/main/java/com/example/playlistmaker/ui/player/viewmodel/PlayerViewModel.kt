package com.example.playlistmaker.ui.player.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.ui.BaseViewModel
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.AddToPlaylist
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : BaseViewModel() {
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _playlistState = MutableLiveData<PlaylistState>()
    val playlistState: LiveData<PlaylistState> = _playlistState

    private val _addingState = MutableLiveData<AddToPlaylist>()
    val addingState: LiveData<AddToPlaylist> get() = _addingState

    private val _selectedPlaylistName = MutableLiveData<String>()
    val selectedPlaylistName: LiveData<String> get() = _selectedPlaylistName


    private var timerJob: Job? = null

    private val _timer = MutableLiveData<String>(CURRENT_TIME)

    val timeProgress: LiveData<String>
        get() = _timer

    init {
        viewModelScope.launch {
            try {
                _isFavorite.value = favoriteTracksInteractor.isFavoriteTrack(track)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }

        playerInteractor.getPlayerState(object : PlayerStateObserver {
            override fun onPlayerStateChanged(state: PlayerState) {
                _playerState.value = state
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun addTrackToPlayList(playlist: Playlist, track: Track) {
        _selectedPlaylistName.postValue(playlist.title)
        viewModelScope.launch {
            try {
                playlistInteractor.addTrackToPlayList(playlist, track).collect {
                    if (it) _addingState.postValue(AddToPlaylist.ADDED)
                    else _addingState.postValue(AddToPlaylist.NOT_ADDED)
                }
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            try {
                playlistInteractor.getPlaylists().collect {
                    if (it.isEmpty()) _playlistState.postValue(PlaylistState.Empty)
                    else _playlistState.postValue(PlaylistState.Content(it))
                }
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun onFavoriteTrackClicked() {
        viewModelScope.launch {
            try {
                if (_isFavorite.value == true) {
                    favoriteTracksInteractor.removeFromFavoriteTrackList(track)
                    _isFavorite.postValue(false)
                } else {
                    favoriteTracksInteractor.addToFavoriteTrackList(track)
                    _isFavorite.postValue(true)
                }
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    fun playbackControl() {
        Log.i("MyLog", "playbackControl lounched")
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            else -> Unit
        }
        updateTimer()
    }

    private fun getTimerPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerInteractor.getCurrentTrackTime())
    }

    private fun releasePlayer() {
        playerInteractor.releasePlayer()
        timerJob = null
        updateTimer()
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
    }

    private fun updateTimer() {
        timerJob?.cancel()
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                timerJob = viewModelScope.launch {
                    while (true) {
                        _timer.value = getTimerPosition()
                        delay(DELAY_300MS)
                    }
                }
            }

            PlayerState.STATE_PAUSED -> timerJob?.cancel()

            else -> _timer.value = CURRENT_TIME
        }
    }


    companion object {
        private const val CURRENT_TIME = "00:00"
        private const val DELAY_300MS = 300L
    }
}

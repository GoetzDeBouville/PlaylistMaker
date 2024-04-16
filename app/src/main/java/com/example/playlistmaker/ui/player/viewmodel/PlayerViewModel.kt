package com.example.playlistmaker.ui.player.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.media.models.AddToPlaylist
import com.example.playlistmaker.domain.media.models.Playlist
import com.example.playlistmaker.domain.media.models.PlaylistState
import com.example.playlistmaker.domain.player.PlayerControl
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playerState = MutableStateFlow(PlayerState.STATE_DEFAULT)
    val playerState: StateFlow<PlayerState>
        get() = _playerState

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

    private val _timer = MutableLiveData(CURRENT_TIME)
    val timeProgress: LiveData<String>
        get() = _timer

    private var playerControl: PlayerControl? = null

    init {
        viewModelScope.launch {
            try {
                _isFavorite.value = favoriteTracksInteractor.isFavoriteTrack(track)
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob = null
    }

    fun playerControlManager(playerControl: PlayerControl) {
        this.playerControl = playerControl
        viewModelScope.launch {
            try {
                playerControl.getPlayerState().collect {
                    _playerState.value = it
                }
            } catch (e: Exception) {
                Log.e("Coroutine Exception", e.stackTraceToString())
            }
        }
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
        playerControl?.pausePlayer()
    }

    fun playbackControl() {
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            else -> Unit
        }
        updateTimer()
    }

    fun setPlayerService(service: PlayerControl) {
        playerControl = service
    }

    private fun getTimerPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerControl?.getCurrentTrackTime())
    }

    private fun startPlayer() {
        playerControl?.startPlayer()
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

    fun removePlayerControl() {
        playerControl = null
    }

    fun showNotification() {
        playerControl?.showNotification()
    }

    fun hideNotification() {
        playerControl?.hideNotification()
    }

    companion object {
        private const val CURRENT_TIME = "00:00"
        private const val DELAY_300MS = 300L
    }
}

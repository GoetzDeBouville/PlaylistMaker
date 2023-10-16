package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteTracksInteractor
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
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private var timerJob: Job? = null

    private val _timer = MutableLiveData<String>(CURRENT_TIME)

    init {
        viewModelScope.launch {
            _isFavorite.value = favoriteTracksInteractor.isFavoriteTrack(track)
        }

        playerInteractor.getPlayerState(object : PlayerStateObserver {
            override fun onPlayerStateChanged(state: PlayerState) {
                _playerState.value = state
            }
        })
    }

    val timeProgress: LiveData<String>
        get() = _timer

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onFavoriteTrackClicked() {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                favoriteTracksInteractor.removeFromFavoriteTrackList(track)
                _isFavorite.postValue(false)
            } else {
                favoriteTracksInteractor.addToFavoriteTrackList(track)
                _isFavorite.postValue(true)
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    fun playbackControl() {
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            else -> {}
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
        playerInteractor.startPlayer() {}
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

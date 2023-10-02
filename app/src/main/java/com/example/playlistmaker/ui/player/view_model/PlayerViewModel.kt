package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private var timerJob: Job? = null

    private val _timer = MutableLiveData<String>(CURRENT_TIME)

    init {
        playerInteractor.getPlayerState(object : PlayerStateObserver {
            override fun onPlayerStateChanged(state: PlayerState) {
                _playerState.value = state
            }
        })
    }

    val timeProgress: LiveData<String>
        get() = _timer

    private fun startPlayer() {
        playerInteractor.startPlayer() {}
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    private fun getTimerPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(playerInteractor.getCurrentTrackTime())
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        updateTimer()
    }

    fun playbackControl() {
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            else -> {}
        }
        updateTimer()
    }

    private fun updateTimer() {
        timerJob?.cancel()
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                timerJob = viewModelScope.launch {
                    while (true) {
                        _timer.value = getTimerPosition()
                        delay(DELAY_10MS)
                    }
                }
            }

            PlayerState.STATE_PAUSED -> timerJob?.cancel()
            
            else -> _timer.value = CURRENT_TIME
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    companion object {
        private const val CURRENT_TIME = "00:00"
        private const val DELAY_10MS = 10L
    }
}

package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.player.models.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { updateTimer() }
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

    private fun releasePlayer() {
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
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                _timer.value = getTimerPosition()
                handler.postDelayed(timerRunnable, DELAY_10MS)
            }
            PlayerState.STATE_PAUSED -> handler.removeCallbacks(timerRunnable)
            else -> {
                handler.removeCallbacks(timerRunnable)
                _timer.value = CURRENT_TIME
            }
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

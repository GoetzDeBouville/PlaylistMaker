package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.util.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track
) : ViewModel() {
    init {
        Log.e("PlayerViewModel", "PlayerViewModel INIT trackID = ${track.trackId}")
    }

    private val playerInteractor = Creator.providePlayerInteractor(track)
    val playerState = playerInteractor.getPlayerState()
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = Runnable { updateTimer() }
    private val _timer = MutableLiveData<String>(CURRENT_TIME)

    val timeProgress: LiveData<String>
        get() = _timer

    fun startPlayer() {
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
        Log.e("PlayerViewModel", "updateTimer called with state ${playerState.value}")
        when (playerState.value) {
            PlayerState.STATE_PLAYING -> {
                Log.e("PlayerViewModel", "Setting timer value: ${getTimerPosition()}")
                _timer.value = getTimerPosition()
                handler.postDelayed(timerRunnable, DELAY)
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
        private const val DELAY = 10L

        fun getViewModelFactory(track: Track): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        track
                    ) as T
                }
            }
    }
}

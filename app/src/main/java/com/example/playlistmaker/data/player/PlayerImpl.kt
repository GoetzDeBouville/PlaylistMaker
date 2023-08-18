package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import java.io.IOException

class PlayerImpl(track: Track) : Player {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrackTime: Long = 0L
    private var startTime: Long = 0L
    private var playerState = MutableLiveData<PlayerState>(PlayerState.STATE_DEFAULT)
    private val observers = mutableListOf<PlayerStateObserver>()
    init {
        preparePlayer(track) {}
    }

    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit) {
        if (track.previewUrl != null) {
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(track.previewUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState.value = PlayerState.STATE_PREPARED
                    callback(true)
                }
                mediaPlayer.setOnCompletionListener {
                    currentTrackTime = 0L
                    startTime = 0L
                    playerState.value = PlayerState.STATE_PREPARED
                    callback(false)
                }
            } catch (e: IOException) {
                callback(false)
            }
        } else {
            callback(false)
        }
    }

    override fun startPlayer(callback: () -> Unit) {
        mediaPlayer.start()
        playerState.value = PlayerState.STATE_PLAYING
        startTime = System.currentTimeMillis() - currentTrackTime
        callback()
    }

    override fun pausePlayer() {
        if (playerState.value == PlayerState.STATE_PLAYING) {
            currentTrackTime = System.currentTimeMillis() - startTime
            mediaPlayer.pause()
            playerState.value = PlayerState.STATE_PAUSED
        }
    }

    override fun getPlayerState(observer: PlayerStateObserver) {
        observers.add(observer)
        playerState.observeForever { state ->
            observers.forEach { it.onPlayerStateChanged(state) }
        }
    }

    override fun getCurrentTrackTime(): Long {
        if (playerState.value == PlayerState.STATE_PLAYING) {
            currentTrackTime = System.currentTimeMillis() - startTime
        }
        return currentTrackTime
    }

    override fun setCurrentTrackTime(time: Long) {
        currentTrackTime = time
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }
}

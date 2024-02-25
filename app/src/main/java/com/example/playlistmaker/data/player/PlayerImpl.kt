package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track

class PlayerImpl(track: Track) : Player {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrackTime: Long = 0L
    private var startTime: Long = 0L
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    private val observers = mutableListOf<PlayerStateObserver>()

    init {
        preparePlayer(track) {}
    }

    override fun getCurrentTrackTime(): Long {
        if (playerState == PlayerState.STATE_PLAYING) {
            currentTrackTime = System.currentTimeMillis() - startTime
        }
        return currentTrackTime
    }

    override fun getPlayerState(observer: PlayerStateObserver) {
        observers.add(observer)
        observer.onPlayerStateChanged(playerState)
    }

    override fun pausePlayer() {
        if (playerState == PlayerState.STATE_PLAYING) {
            currentTrackTime = System.currentTimeMillis() - startTime
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
            notifyPlayerStateChanged(playerState)
        }
    }

    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit) {
        if (track.previewUrl != null) {
            playerState = PlayerState.STATE_DEFAULT
            notifyPlayerStateChanged(playerState)
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(track.previewUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState = PlayerState.STATE_PREPARED
                    notifyPlayerStateChanged(playerState)
                    callback(true)
                }
                mediaPlayer.setOnCompletionListener {
                    currentTrackTime = 0L
                    startTime = 0L
                    playerState = PlayerState.STATE_PREPARED
                    notifyPlayerStateChanged(playerState)
                    callback(false)
                }
            } catch (e: Exception) {
                callback(false)
            }
        } else {
            callback(false)
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun setCurrentTrackTime(time: Long) {
        currentTrackTime = time
    }
    
    override fun startPlayer() {
        try {
            if (!mediaPlayer.isPlaying) mediaPlayer.start()
            playerState = PlayerState.STATE_PLAYING
            startTime = System.currentTimeMillis() - currentTrackTime
            notifyPlayerStateChanged(playerState)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun notifyPlayerStateChanged(state: PlayerState) {
        playerState = state
        observers.forEach { it.onPlayerStateChanged(state) }
    }
}

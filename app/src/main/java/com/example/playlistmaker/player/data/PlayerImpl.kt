package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.player.domain.models.Track
import java.io.IOException

class PlayerImpl : Player {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrackTime: Long = 0L
    private var startTime: Long = 0L
    private var playerState = STATE_DEFAULT

    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit) {
        if (track.previewUrl != null) {
            try {
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(track.previewUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState = STATE_PREPARED
                    callback(true)
                }
                mediaPlayer.setOnCompletionListener {
                    playerState = STATE_PREPARED
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
        playerState = STATE_PLAYING
        startTime = System.currentTimeMillis() - currentTrackTime
        callback()
    }

    override fun pausePlayer() {
        if (playerState == STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = STATE_PAUSED
        }
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTrackTime(): Long {
        return currentTrackTime
    }

    override fun setCurrentTrackTime(time: Long) {
        currentTrackTime = time
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}

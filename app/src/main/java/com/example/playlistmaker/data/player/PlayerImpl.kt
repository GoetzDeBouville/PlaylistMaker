package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import java.io.IOException

class PlayerImpl(track: Track) : Player {

    private lateinit var mediaPlayer: MediaPlayer
    private var currentTrackTime: Long = 0L
    private var startTime: Long = 0L
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    private val observers = mutableListOf<PlayerStateObserver>()

    init {
        preparePlayer(track) {}
    }

    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit) {
        if (track.previewUrl != null) {
            try {
                if (::mediaPlayer.isInitialized) {
                    mediaPlayer.release()
                }
                mediaPlayer = MediaPlayer()
                mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(track.previewUrl)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    Log.i("PlayerState", "Prepared")
                    playerState = PlayerState.STATE_PREPARED
                    notifyPlayerStateChanged(playerState)
                    callback(true)
                }
                mediaPlayer.setOnCompletionListener {
                    Log.d("PlayerState", "Completed")
                    currentTrackTime = 0L
                    startTime = 0L
                    playerState = PlayerState.STATE_PREPARED
                    notifyPlayerStateChanged(playerState)
                    callback(false)
                }
                mediaPlayer.setOnErrorListener { mp, what, extra ->
                    Log.e("PlayerState", "ERROR What: $what, Extra: $extra")
                    true
                }

            } catch (e: IOException) {
                callback(false)
            } catch (e: Exception) {
                callback(false)
            }
        } else {
            callback(false)
        }
    }

    override fun startPlayer(callback: () -> Unit) {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        playerState = PlayerState.STATE_PLAYING
        startTime = System.currentTimeMillis() - currentTrackTime
        notifyPlayerStateChanged(playerState)
        callback()
    }

    override fun pausePlayer() {
        if (playerState == PlayerState.STATE_PLAYING) {
            currentTrackTime = System.currentTimeMillis() - startTime
            mediaPlayer.pause()
            playerState = PlayerState.STATE_PAUSED
            notifyPlayerStateChanged(playerState)
        }
    }

    override fun getPlayerState(observer: PlayerStateObserver) {
        observers.add(observer)
        observer.onPlayerStateChanged(playerState)
    }

    override fun getCurrentTrackTime(): Long {
        if (playerState == PlayerState.STATE_PLAYING) {
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

    private fun notifyPlayerStateChanged(state: PlayerState) {
        playerState = state
        observers.forEach { it.onPlayerStateChanged(state) }
    }
}

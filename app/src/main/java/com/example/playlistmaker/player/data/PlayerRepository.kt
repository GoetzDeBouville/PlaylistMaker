package com.example.playlistmaker.player.data

import com.example.playlistmaker.Track

interface PlayerRepository {
    fun preparePlayer(track: Track, callback: (Boolean) -> Unit)
    fun startPlayer(callback: () -> Unit)
    fun pausePlayer()
    fun getPlayerState(): Int
    fun getCurrentTrackTime(): Long
    fun setCurrentTrackTime(time: Long)
    fun releasePlayer()
}


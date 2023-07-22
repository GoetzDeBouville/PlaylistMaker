package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.models.Track

interface PlayerInteractor {
    fun preparePlayer(track: Track, callback: (Boolean) -> Unit)
    fun startPlayer(callback: () -> Unit)
    fun pausePlayer()
    fun getPlayerState(): Int
    fun getCurrentTrackTime(): Long
    fun setCurrentTrackTime(time: Long)
    fun releasePlayer()
}

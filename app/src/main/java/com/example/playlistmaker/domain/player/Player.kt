package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.search.models.Track

interface Player {
    fun preparePlayer(track: Track, callback: (Boolean) -> Unit)
    fun startPlayer(callback: () -> Unit)
    fun pausePlayer()
    fun getPlayerState(): Int
    fun getCurrentTrackTime(): Long
    fun setCurrentTrackTime(time: Long)
    fun releasePlayer()
}


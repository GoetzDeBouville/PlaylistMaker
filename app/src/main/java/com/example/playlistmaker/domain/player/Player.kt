package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.search.models.Track

interface Player {
    fun getCurrentTrackTime(): Long
    fun getPlayerState(observer: PlayerStateObserver)
    fun pausePlayer()
    fun preparePlayer(track: Track)
    fun releasePlayer()
    fun setCurrentTrackTime(time: Long)
    fun startPlayer()
}

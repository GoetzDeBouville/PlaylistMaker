package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.search.models.Track

interface PlayerInteractor {
    fun getCurrentTrackTime(): Long
    fun getPlayerState(observer: PlayerStateObserver)
    fun pausePlayer()
    fun preparePlayer(track: Track, callback: (Boolean) -> Unit)
    fun setCurrentTrackTime(time: Long)
    fun startPlayer(callback: () -> Unit)
    fun releasePlayer()
}

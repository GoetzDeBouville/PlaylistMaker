package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.models.Track

interface PlayerInteractor : Player {
    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit)
    override fun startPlayer(callback: () -> Unit)
    override fun pausePlayer()
    override fun getPlayerState(): Int
    override fun getCurrentTrackTime(): Long
    override fun setCurrentTrackTime(time: Long)
    override fun releasePlayer()
}

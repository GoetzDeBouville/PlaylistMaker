package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.models.Track

class PlayerInteractorImpl(private val player: Player) : PlayerInteractor {
    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit) {
        player.preparePlayer(track, callback)
    }
    override fun startPlayer(callback: () -> Unit) {
        player.startPlayer(callback)
    }
    override fun pausePlayer() {
        player.pausePlayer()
    }
    override fun getPlayerState(): Int {
        return player.getPlayerState()
    }
    override fun getCurrentTrackTime(): Long {
        return player.getCurrentTrackTime()
    }
    override fun setCurrentTrackTime(time: Long) {
        player.setCurrentTrackTime(time)
    }
    override fun releasePlayer() {
        player.releasePlayer()
    }
}

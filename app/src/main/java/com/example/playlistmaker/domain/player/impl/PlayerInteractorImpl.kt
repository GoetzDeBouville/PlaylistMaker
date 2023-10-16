package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerStateObserver
import com.example.playlistmaker.domain.search.models.Track

class PlayerInteractorImpl(private val player: Player) : PlayerInteractor {
    override fun getCurrentTrackTime(): Long {
        return player.getCurrentTrackTime()
    }

    override fun getPlayerState(observer: PlayerStateObserver){
        return player.getPlayerState(observer)
    }

    override fun pausePlayer() {
        player.pausePlayer()
    }

    override fun preparePlayer(track: Track, callback: (Boolean) -> Unit) {
        player.preparePlayer(track, callback)
    }

    override fun releasePlayer() {
        player.releasePlayer()
    }

    override fun setCurrentTrackTime(time: Long) {
        player.setCurrentTrackTime(time)
    }

    override fun startPlayer(callback: () -> Unit) {
        player.startPlayer(callback)
    }
}

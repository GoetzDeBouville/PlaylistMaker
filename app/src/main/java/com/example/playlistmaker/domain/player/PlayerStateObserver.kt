package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.models.PlayerState

interface PlayerStateObserver {
    fun onPlayerStateChanged(state: PlayerState)
}
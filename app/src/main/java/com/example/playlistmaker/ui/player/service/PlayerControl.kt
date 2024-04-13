package com.example.playlistmaker.ui.player.service

import com.example.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
}
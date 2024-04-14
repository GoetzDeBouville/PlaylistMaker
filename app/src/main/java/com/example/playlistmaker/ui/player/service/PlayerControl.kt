package com.example.playlistmaker.ui.player.service

import android.app.Notification
import com.example.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface PlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun showNotification()
    fun hideNotification()

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "player_service_channel"
        const val SERVICE_NOTIFICATION_ID = 100
    }
}
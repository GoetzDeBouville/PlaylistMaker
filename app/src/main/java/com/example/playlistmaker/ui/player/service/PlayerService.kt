package com.example.playlistmaker.ui.player.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.media3.common.Player
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerService(private val interactor: PlayerInteractor) : Service(), PlayerControl {
    private val binder = PlayerServiceBinder()
    private var track: Track? = null

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.STATE_DEFAULT)
    val playerState: StateFlow<PlayerState>
        get() = _playerState

    override fun onBind(intent: Intent?): IBinder {
        val trackJson = intent?.getStringExtra(SearchFragment.TRACK_KEY)
        track = Gson().fromJson(trackJson, Track::class.java)

        track?.let { interactor.preparePlayer(track!!) {} }
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        stopSelf()
        releasePlayer()
        return super.onUnbind(intent)
    }

    inner class PlayerServiceBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        return playerState
    }

    override fun startPlayer() {
        interactor.startPlayer()
        _playerState.value = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        interactor.pausePlayer()
        _playerState.value = PlayerState.STATE_PAUSED
    }

    private fun createNotification() : Notification {
        return NotificationCompat.Builder(this, PlayerControl.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("${track?.artistName} - ${track?.trackName}")
            .setSmallIcon(R.drawable.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            PlayerControl.SERVICE_NOTIFICATION_ID,
            createNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private fun releasePlayer() {
        interactor.releasePlayer()
        _playerState.value = PlayerState.STATE_DEFAULT
    }
}
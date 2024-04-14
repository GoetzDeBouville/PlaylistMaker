package com.example.playlistmaker.ui.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import com.example.playlistmaker.data.player.PlayerImpl
import com.example.playlistmaker.domain.player.PlayerControl
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.search.fragment.SearchFragment
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerService : Service(), PlayerControl {
    private val binder = PlayerServiceBinder()
    private var track: Track? = null
    private var player = PlayerImpl()

    private val _mediaPlayerState = MutableStateFlow(PlayerState.STATE_DEFAULT)
    private val mediaPlayerState: StateFlow<PlayerState>
        get() = _mediaPlayerState

    override fun onBind(intent: Intent?): IBinder {
        val trackJson = intent?.getStringExtra(SearchFragment.TRACK_KEY)
        track = Gson().fromJson(trackJson, Track::class.java)
        track?.let {
            player.preparePlayer(track!!) {
                pausePlayer()
            }
            _mediaPlayerState.value = PlayerState.STATE_PREPARED
        }
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val channel = NotificationChannel(
            PlayerControl.NOTIFICATION_CHANNEL_ID,
            this.resources.getString(R.string.app_name),
            NotificationManager.IMPORTANCE_MIN
        )
        channel.description = "Service for playing music"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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
        return mediaPlayerState
    }

    override fun startPlayer() {
        player.startPlayer()
        _mediaPlayerState.value = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        player.pausePlayer()
        _mediaPlayerState.value = PlayerState.STATE_PAUSED
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, PlayerControl.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("${track?.artistName} - ${track?.trackName}")
            .setSmallIcon(R.drawable.ic_launcher)
            .setLargeIcon(getDrawable(R.drawable.ic_launcher)?.toBitmap())
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

    override fun getCurrentTrackTime(): Long {
        return player.getCurrentTrackTime()
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    private fun releasePlayer() {
        player.releasePlayer()
        _mediaPlayerState.value = PlayerState.STATE_DEFAULT
    }
}
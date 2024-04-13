package com.example.playlistmaker.ui.player.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
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
        val gson = Gson()
        track = gson.fromJson(trackJson, Track::class.java)

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
    }

    override fun pausePlayer() {
        interactor.pausePlayer()
    }

    private fun releasePlayer() {
        interactor.releasePlayer()
    }
}
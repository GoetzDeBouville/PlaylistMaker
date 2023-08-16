package com.example.playlistmaker.domain.player

import androidx.lifecycle.LiveData
import com.example.playlistmaker.domain.player.models.PlayerState
import com.example.playlistmaker.domain.search.models.Track

interface PlayerInteractor {
    fun preparePlayer(track: Track, callback: (Boolean) -> Unit)
    fun startPlayer(callback: () -> Unit)
    fun pausePlayer()
    fun getPlayerState(): LiveData<PlayerState>
    fun getCurrentTrackTime(): Long
    fun setCurrentTrackTime(time: Long)
    fun releasePlayer()
}

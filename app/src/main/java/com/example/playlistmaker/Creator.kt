package com.example.playlistmaker

import com.example.playlistmaker.player.domain.Player
import com.example.playlistmaker.player.data.PlayerImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.PlayerInteractorImpl

object Creator {
    private fun getPlayer(): Player {
        return PlayerImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayer())
    }
}

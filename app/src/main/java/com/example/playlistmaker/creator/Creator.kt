package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
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

    fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator(context), context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val settingsRepository = SettingsRepositoryImpl(context.applicationContext as App)
        return SettingsInteractor(settingsRepository)
    }
}

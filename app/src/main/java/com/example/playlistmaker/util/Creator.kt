package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.repository.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.repository.SearchRepositoryImpl
import com.example.playlistmaker.data.search.storage.History
import com.example.playlistmaker.data.search.storage.shared_preferences.SharedPreferencesHistoryStorage
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.search.api.HistoryInteractor
import com.example.playlistmaker.domain.search.api.HistoryRepository
import com.example.playlistmaker.domain.search.api.SearchInteractor
import com.example.playlistmaker.domain.search.api.SearchRepository
import com.example.playlistmaker.domain.search.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.data.player.PlayerImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl

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

    private fun provideHistoryStorage(context: Context): History {
        return SharedPreferencesHistoryStorage(context)
    }

    private fun provideHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(
            provideHistoryStorage(context)
        )
    }


    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(
            provideSearchRepository(context), context
        )
    }

    private fun provideSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
            RetrofitNetworkClient(context)
        )
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(
            provideHistoryRepository(context)
        )
    }
}

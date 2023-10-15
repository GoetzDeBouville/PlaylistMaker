package com.example.playlistmaker

import android.app.Application
import androidx.room.Room
import androidx.room.getQueryDispatcher
import com.example.playlistmaker.db.AppDatabase
import com.example.playlistmaker.db.entity.TrackEntity
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(dataModule, repositoryModule, interactorModule, viewModelModule))
        }

        val settingsRepository: SettingsRepository by inject()
        val darkTheme = settingsRepository.getThemeSettings()
        settingsRepository.updateThemeSettings(darkTheme)
    }
}

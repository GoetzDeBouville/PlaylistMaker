package com.example.playlistmaker.data.settings.impl

import android.content.Context
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(private val app: App) : SettingsRepository {

    private val sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(isDarkTheme = sharedPreferences.getBoolean(THEME_KEY, false))
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        app.switchTheme(settings.isDarkTheme)
        sharedPreferences.edit().putBoolean(THEME_KEY, settings.isDarkTheme).apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "app_preferences"
        private const val THEME_KEY = "theme_key"
    }
}

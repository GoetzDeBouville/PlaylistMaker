package com.example.playlistmaker.data.settings.impl

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.models.ThemeSettings

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    private var darkTheme = false
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(isDarkTheme = sharedPreferences.getBoolean(THEME_KEY, false))
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        switchTheme(settings.isDarkTheme)
        sharedPreferences.edit().putBoolean(THEME_KEY, settings.isDarkTheme).apply()
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "app_preferences"
        private const val THEME_KEY = "theme_key"
    }
}

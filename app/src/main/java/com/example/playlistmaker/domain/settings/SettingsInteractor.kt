package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractor(private val settingsRepository: SettingsRepository) {
    fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    fun updateThemeSettings(settings: ThemeSettings) {
        settingsRepository.updateThemeSettings(settings)
    }
}

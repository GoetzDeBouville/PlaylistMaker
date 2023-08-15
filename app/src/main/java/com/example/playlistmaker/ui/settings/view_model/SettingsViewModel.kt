package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    val themeSettings = MutableLiveData<Boolean>()

    init {
        loadThemeSettings()
    }
    private fun loadThemeSettings() {
        themeSettings.value = settingsInteractor.getThemeSettings().isDarkTheme
    }

    fun updateThemeSettings(isDarkTheme: Boolean) {
        settingsInteractor.updateThemeSettings(ThemeSettings(isDarkTheme))
        themeSettings.value = isDarkTheme
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }
}

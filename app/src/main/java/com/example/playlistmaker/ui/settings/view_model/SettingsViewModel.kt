package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _themeSettings = MutableLiveData<Boolean>()
    val themeSettings: LiveData<Boolean> get() = _themeSettings

    init {
        loadThemeSettings()
    }
    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun updateThemeSettings(isDarkTheme: Boolean) {
        settingsInteractor.updateThemeSettings(ThemeSettings(isDarkTheme))
        _themeSettings.value = isDarkTheme
    }

    private fun loadThemeSettings() {
        _themeSettings.value = settingsInteractor.getThemeSettings().isDarkTheme
    }
}

package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsViewModel(private val application: Application) : ViewModel() {

    private val sharingInteractor = Creator.provideSharingInteractor(application)
    private val settingsRepository = Creator.provideSettingsRepository(application)

    val themeSettings = MutableLiveData<Boolean>()

    init {
        loadThemeSettings()
    }
    private fun loadThemeSettings() {
        themeSettings.value = settingsRepository.getThemeSettings().isDarkTheme
    }

    fun updateThemeSettings(isDarkTheme: Boolean) {
        settingsRepository.updateThemeSettings(ThemeSettings(isDarkTheme))
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

package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.models.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.ui.settings.model.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState(themeIsDark = false))
    val uiState = _uiState.asStateFlow()

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
        _uiState.update { _uiState.value.copy(themeIsDark = isDarkTheme) }
    }

    private fun loadThemeSettings() {
        val isDarkTheme = settingsInteractor.getThemeSettings().isDarkTheme
        _uiState.update { SettingsState(themeIsDark = isDarkTheme) }
    }
}

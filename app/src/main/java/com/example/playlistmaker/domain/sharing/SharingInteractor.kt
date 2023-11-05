package com.example.playlistmaker.domain.sharing

interface SharingInteractor {
    fun openSupport()
    fun openTerms()
    fun shareApp()
    fun sharePlaylist(playlistData: String)
}

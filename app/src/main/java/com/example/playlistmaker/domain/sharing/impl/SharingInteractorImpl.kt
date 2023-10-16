package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.domain.sharing.ContentProvider
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val contentProvider: ContentProvider
) : SharingInteractor {
    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData(), getTextChosingApp(), getNoAppText())
    }

    override fun openTerms() {
        externalNavigator.openTerms(getTermsLink())
    }

    override fun shareApp() {
        externalNavigator.shareApp(getShareAppLink(), getShareAppTitle())
    }

    private fun getNoAppText(): String {
        return contentProvider.getNoAppText()
    }

    private fun getShareAppLink(): String {
        return contentProvider.getShareAppLink()
    }

    private fun getShareAppTitle(): String {
        return contentProvider.getShareAppTitle()
    }

    private fun getSupportEmailData(): EmailData {
        return contentProvider.getSupportEmailData()
    }

    private fun getTextChosingApp(): String {
        return contentProvider.getTextChosingApp()
    }

    private fun getTermsLink(): String {
        return contentProvider.getTermsLink()
    }
}

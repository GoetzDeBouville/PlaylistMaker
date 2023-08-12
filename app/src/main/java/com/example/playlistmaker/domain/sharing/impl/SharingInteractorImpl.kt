package com.example.playlistmaker.domain.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareApp(getShareAppLink(), getShareAppTitle())
    }

    override fun openTerms() {
        externalNavigator.openTerms(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData(), getTextChosingApp(), getNoAppText())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.android_practicum_link)
    }

    private fun getShareAppTitle(): String {
        return context.getString(R.string.share_app)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.practicum_offer_link)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.target_email),
            subject = context.getString(R.string.subject),
            text = context.getString(R.string.text_message)
        )
    }

    private fun getTextChosingApp(): String {
        return context.getString(R.string.text_email_app)
    }

    private fun getNoAppText(): String {
        return context.getString(R.string.text_no_email_app)
    }
}

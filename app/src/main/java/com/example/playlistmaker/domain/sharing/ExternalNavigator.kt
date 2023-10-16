package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun openEmail(emailData: EmailData, chooserTitle: String, noEmailAppMessage: String)
    fun openTerms(termsLink: String)
    fun shareApp(link: String, title: String)
}

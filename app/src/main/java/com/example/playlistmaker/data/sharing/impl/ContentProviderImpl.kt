package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ContentProvider
import com.example.playlistmaker.domain.sharing.model.EmailData

class ContentProviderImpl(private val context: Context) : ContentProvider {
    override fun getShareAppLink(): String {
        return context.getString(R.string.android_practicum_link)
    }

    override fun getShareAppTitle(): String {
        return context.getString(R.string.share_app)
    }

    override fun getTermsLink(): String {
        return context.getString(R.string.practicum_offer_link)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.target_email),
            subject = context.getString(R.string.subject),
            text = context.getString(R.string.text_message)
        )
    }

    override fun getTextChosingApp(): String {
        return context.getString(R.string.text_email_app)
    }

    override fun getNoAppText(): String {
        return context.getString(R.string.text_no_email_app)
    }
}
package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ContentProvider {
    fun getShareAppLink(): String
    fun getShareAppTitle(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
    fun getTextChosingApp(): String
    fun getNoAppText(): String
}

package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ContentProvider {
    fun getNoAppText(): String
    fun getShareAppLink(): String
    fun getShareAppTitle(): String
    fun getSupportEmailData(): EmailData
    fun getTextChosingApp(): String
    fun getTermsLink(): String
}

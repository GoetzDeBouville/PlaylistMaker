package com.example.playlistmaker.data.search.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.ErrorMessageProvider

class ErrorMessageProviderImpl(private val context: Context) : ErrorMessageProvider {
    override fun getConnectionErrorMessage(): String {
        return context.getString(R.string.check_connection)
    }

    override fun getEmptyListMessage(): String {
        return context.getString(R.string.nothing_found)
    }
}
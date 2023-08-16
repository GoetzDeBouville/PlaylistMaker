package com.example.playlistmaker.domain.search

interface ErrorMessageProvider {
    fun getConnectionErrorMessage(): String
    fun getEmptyListMessage(): String
}
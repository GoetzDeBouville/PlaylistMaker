package com.example.playlistmaker.util

sealed class Resource<T>(val data: T? = null, val errorType: LoadingStatus? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(type: LoadingStatus, data: T? = null) : Resource<T>(data, type)
}

enum class LoadingStatus {
    FAILED_SEARCH,
    SUCCESS,
    NO_INTERNET,
    SERVER_ERROR
}

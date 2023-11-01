package com.example.playlistmaker.domain.media.models

import android.net.Uri

data class Playlist(
    val id: Int,
    val title: String,
    val description: String?,
    val imagePath: Uri?,
    val trackIds: String?,
    var trackAmount: Int
)

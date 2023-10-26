package com.example.playlistmaker.domain.media.models

import android.net.Uri

data class Playlist(
    val id: Int,
    val title: String,
    val description: String?,
    val imagePath: Uri?,
    var trackIds: String?,
    var trackAmount: Int
)

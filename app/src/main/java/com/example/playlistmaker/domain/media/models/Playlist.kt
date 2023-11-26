package com.example.playlistmaker.domain.media.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Int,
    val title: String,
    val description: String?,
    val imagePath: Uri?,
    val trackAmount: Int
) : Parcelable

package com.example.playlistmaker.domain.search.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var isFavorite: Boolean = false
) : Parcelable {
    fun getArtwork512() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun timeFormater(): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)

    fun yearFormater() = releaseDate?.substringBefore("-")
}

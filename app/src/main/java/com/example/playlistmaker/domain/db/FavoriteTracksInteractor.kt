package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun favoriteTracks(): Flow<List<Track>>

    suspend fun addToFavoriteTrackList(track: Track)

    suspend fun removeFromFavoriteTrackList(track: Track)
}

package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun addToFavoriteTrackList(track: Track)

    suspend fun isFavoriteTrack(track: Track) : Boolean

    suspend fun removeFromFavoriteTrackList(track: Track)
}

package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksRepository {
    suspend fun addTrackToFavorites(track: Track)
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun isFavoriteTrack(track: Track): Boolean
    suspend fun removeTrackFromFavorites(track: Track)
}

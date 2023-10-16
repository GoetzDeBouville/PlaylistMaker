package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteTracksRepository {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun addTrackToFavorites(track: Track)
    suspend fun removeTrackFromFavorites(track: Track)
    suspend fun isFavoriteTrack(track: Track): Boolean
}
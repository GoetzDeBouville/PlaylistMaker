package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.media.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addNewPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun removePlaylist(playlist: Playlist)
}